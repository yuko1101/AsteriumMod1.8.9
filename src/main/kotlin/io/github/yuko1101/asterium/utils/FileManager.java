package io.github.yuko1101.asterium.utils;

import com.google.gson.Gson;

import java.io.*;

public class FileManager {

    private static final Gson gson = new Gson();

    private static final File ROOT_DIR = new File("Asterium");
    private static final File MODS_DIR = new File(ROOT_DIR, "Mods");
    private static final File ADDONS_DIR = new File(ROOT_DIR, "Addons");
    private static final File COSMETICS_DIR = new File(ROOT_DIR, "Cosmetics");

    public static void init() {

        if (!ROOT_DIR.exists()) { ROOT_DIR.mkdirs(); }
        if (!MODS_DIR.exists()) { MODS_DIR.mkdirs(); }
        if (!ADDONS_DIR.exists()) { ADDONS_DIR.mkdirs(); }
        if (!COSMETICS_DIR.exists()) { COSMETICS_DIR.mkdirs(); }


    }

    public static Gson getGson() {
        return gson;
    }

    public static File getModsDirectory() {
        return MODS_DIR;
    }

    public static File getAddonsDirectory() {
        return ADDONS_DIR;
    }

    public static File getCosmeticsDirectory() {
        return COSMETICS_DIR;
    }

    public static File getClientDirectory() {
        return ROOT_DIR;
    }

    public static boolean writeJsonToFile(File file, Object obj) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(gson.toJson(obj).getBytes());
            outputStream.flush();
            outputStream.close();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static <T extends Object> T readFromJson(File file, Class<T> c) {

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }

            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();

            return gson.fromJson(builder.toString(), c);

        }
        catch(IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static <T extends Object> T readFromJson(File file, Class<T> c, Object obj) {

        try {
            if (!file.exists()) {
                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(gson.toJson(obj).getBytes());
                outputStream.flush();
                outputStream.close();

            }

            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }

            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();

            return gson.fromJson(builder.toString(), c);

        }
        catch(IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static boolean writeToFile(File file, String str) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(str.getBytes());
            outputStream.flush();
            outputStream.close();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
    public static String readFromJson(File file, String str) {

        try {
            if (!file.exists()) {
                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(str.getBytes());
                outputStream.flush();
                outputStream.close();

            }

            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }

            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();

            return builder.toString();

        }
        catch(IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
