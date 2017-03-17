# Lines of Code

## Idea
Collect information about filtered files in the whole file-tree of a given directory.

## Usage
`$ loc config dir`

Where:

`config` is a path to a text file with information about filters to apply to each file in file-tree.

`dir` is a path to directory under which to look for files.

## Filter
A filter has a following format `[ ]prefix[ ]filter[ ]`

Where:

`prefix` is a character describing the filter (e.g. `.`)

`filter` is a string representing filter

`[ ]` is a sequence of whitespace characters (can be empty)

### File Extension Filter
#### Format 
`.extension` (e.g. `.cpp`, `.java`)
#### Description
Filters files with given file extension.

### Time Modified Filter
#### Format 
`[<>][ ]seconds` (e.g. `>0`, `<   1400000000`)
#### Description
Filters files which were modified later/earlier than given unix-time in seconds from 1 Jan 1970 00:00:00

### Aggregate Filter
#### Format
`[&|][ ](filter1[ filter2[ ...]])` (e.g. `$(.java >1400000000)`, `| (.cpp .c .cc)`)

`![ ]filter` (e.g. `!.cpp`)
#### Description
Allows to create nested sequences of filters.

## Examples
### Possible config file
  ```
  .java
      .txt  
  >140505040
  &(  .css <15000000  )
  |( & (.h .c)  &     ( .hpp .cpp) .jar )
  ```

### Possible output

  ```
  Total - 8637 lines in 110 files
  ---------------
  >140505040                        - 8637 lines in 110 files
  |( &( .h .c) &( .hpp .cpp) .jar ) - 4344 lines in 4 files
  .java                             - 1646 lines in 36 files
  .txt                              - 72 lines in 16 files
  ```
