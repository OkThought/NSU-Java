package ru.nsu.ccfit.bogush;

import ru.nsu.ccfit.bogush.view.FactoryView;

import java.io.IOException;

public class Main {
	private static final String CONFIG_FILE = "config.properties";
	private static CarFactoryModel model;

	private static void prepare(String configFilePath) throws IOException {
		Config config = new ConfigSerializer().load(configFilePath);
		model = new CarFactoryModel(config);
	}

	public static void main(String[] args) {
		try {
			prepare(CONFIG_FILE);
			new FactoryView(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
