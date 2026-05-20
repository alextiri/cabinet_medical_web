package com.cabinet.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.List;

public class ExportService {
    public <T> void exportToCSV(List<T> items, File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            if (items == null || items.isEmpty()) {
                return;
            }

            Field[] fields = items.getFirst()
                            .getClass()
                            .getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
                writer.print(fields[i].getName());
                if (i < fields.length - 1) {
                    writer.print(",");
                }
            }

            writer.println();
            for (T item : items) {
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    Object value = fields[i].get(item);

                    writer.print(value != null ? value.toString() : "");
                    if (i < fields.length - 1) {
                        writer.print(",");
                    }
                }

                writer.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void exportToJSON(List<T> items, File file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void exportToTXT(List<T> items, File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            if (items == null || items.isEmpty()) {
                return;
            }

            Field[] fields = items.getFirst()
                            .getClass()
                            .getDeclaredFields();

            for (T item : items) {
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(item);

                    writer.println(field.getName() + ": " + (value != null ? value.toString() : ""));
                }
                writer.println("--------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void exportToXML(List<T> items, File file) {
        try {
            XmlMapper mapper = new XmlMapper();

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}