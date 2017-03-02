package bogush;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class FilterParseException extends Exception {
    FilterParseException(String message) {
        super(message);
    }
}

class FilterParser {
    static Filter[] parse(Path configFile) throws IOException, FilterParseException {
        ArrayList<Filter> filterList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(configFile.toFile()))) {
            String line;
            while((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    filterList.add(parse(line));
                }
            }
        }
        if (filterList.size() == 0) {
            throw new FilterParseException("No filters have been parsed");
        }
        Filter[] filters = new Filter[filterList.size()];
        filters = filterList.toArray(filters);
        return filters;
    }

    static Filter parse(String filterString) throws FilterParseException, IOException {
        Filter filter;
        filterString = filterString.trim();
        if (filterString.startsWith("<")) {
            if (filterString.charAt(1) == '=') {
                filterString = filterString.replaceFirst("<= *", "");
                filter = new FilterNot(new TimeFilterGreater(Integer.parseInt(filterString)));
            } else {
                filterString = filterString.replaceFirst("< *", "");
                filter = new TimeFilterLess(Integer.parseInt(filterString));
            }
        } else if (filterString.startsWith(">")) {
            if (filterString.charAt(1) == '=') {
                filterString = filterString.replaceFirst(">= *", "");
                filter = new FilterNot(new TimeFilterLess(Integer.parseInt(filterString)));
            } else {
                filterString = filterString.replaceFirst("> *", "");
                filter = new TimeFilterGreater(Integer.parseInt(filterString));
            }
        } else if (filterString.startsWith(".")) {
            filter = new FileExtensionFilter(filterString);
        } else if (filterString.startsWith("And")) {
            filterString = filterString.replaceFirst("And\\s*\\(", "");
            filter = new FilterAnd(parseSequence(openParenthesis(filterString, true)));
        } else if (filterString.startsWith("Or")) {
            filterString = filterString.replaceFirst("Or\\s*\\(", "");
            filter = new FilterOr(parseSequence(openParenthesis(filterString, true)));
        } else if (filterString.startsWith("Not")) {
            filterString = filterString.replaceFirst("Not\\s*\\(", "");
            Filter[] filters = parseSequence(openParenthesis(filterString, true));
            if (filters.length != 1) {
                throw new FilterParseException("Only one filter can be passed to Not() operation");
            }
            filter = new FilterNot(filters[0]);
        } else if (filterString.startsWith("(")) {
            filterString = openParenthesis(filterString, false);
            filter = parse(filterString);
        } else {
            throw new FilterParseException("Couldn't parse filter '" + filterString + "'");
        }
        return filter;
    }

    private static String openParenthesis(String filterString, boolean openRemoved) throws IOException {
        StringReader reader = new StringReader(filterString);
        int parenthesisCounter = openRemoved ? 1 : 0; // one parenthesis is read
        int open = openRemoved ? 0 : 1;
        int close = 0;
        int cur;
        while((cur = reader.read()) != -1) {
            if (cur == '(') {
                if (++parenthesisCounter == 0) break;
            } else if (cur == ')') {
                if (--parenthesisCounter == 0) break;
            }
            close++;
        }
        return filterString.substring(open, close);
    }

    private static Filter[] parseSequence(String filtersString) throws FilterParseException, IOException {
        List<Filter> filters = new ArrayList<>();
        StringReader reader = new StringReader(filtersString);
        int parenthesisCounter = 0;
        int currentIndex = 0;
        int filterBegin = 0;
        int filterEnd;
        int currentChar;
        while(true) {
            currentChar = reader.read();
            if (currentChar == '(') {
                ++parenthesisCounter;
            } else if (currentChar == ')') {
                --parenthesisCounter;
            } else if ((currentChar == ',' && parenthesisCounter == 0) || currentChar == -1) {
                filterEnd = currentIndex;
                String filterString = filtersString.substring(filterBegin, filterEnd);
                filters.add(parse(filterString));
                filterBegin = currentIndex+1;
                if (currentChar == -1) break;
            }
            currentIndex++;
        }
        return filters.toArray(new Filter[filters.size()]);
    }
}
