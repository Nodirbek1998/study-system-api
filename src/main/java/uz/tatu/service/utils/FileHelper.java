package uz.tatu.service.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileHelper {

    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "ru";

    public static final String MENU = "menu";
    public static final String ACTIVE = "active";
    public static final String X_AUTH_USER_TYPE = "X-AUTH-USER-TYPE";
    public static final String MOBILE_USER = "MOBILE-USER";

    public static final String ROOT_UPLOAD_PATH = "/upload/files/";

    private static boolean isWindow(){
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("linux")
            || osName.contains("mpe/ix")
            || osName.contains("freebsd")
            || osName.contains("irix")
            || osName.contains("digital unix")
            || osName.contains("unix")
            || osName.contains("mac os x")) {
            return false;
        }
        return true;
    }
    public static String getFilesDirectory(){
        if (isWindow()) {
            return "\\upload\\files";
        }
        return "upload/files/";
    }

    public static String getPhotoDirectory(){
        if (isWindow()) {
            return "/upload/photo/";
        }
        return "upload/photo/";
    }

    public static String getTempDirectory(){
        if (isWindow()) {
            return "/upload/temp/";
        }
        return "upload/temp/";
    }

    public static String getFontDirectory(){
        if (isWindow()) {
            return "/upload/fonts/";
        }
        return "upload/fonts/";
    }

    public static String getSignDirectory(){
        if (isWindow()) {
            return "/upload/sign/";
        }
        return "upload/sign/";
    }


    public static String getUploadFilePath(String filePath, LocalDateTime date){
        if(date == null || filePath == null){
            return "";
        }
        Integer year = date.getYear();
        Integer month = date.getMonthValue();
        Integer day = date.getDayOfMonth();

        String currentDateDir;
        if (isWindow()) {
            currentDateDir = String.format("%s\\%s\\%s\\%s\\", filePath, year, String.format("%02d", month), String.format("%02d", day));
        } else {
            currentDateDir = String.format("%s/%s/%s/%s/", filePath, year, String.format("%02d", month), String.format("%02d", day));
        }

        File dir = new File(currentDateDir);
        if(!dir.exists()){
            dir.mkdirs();
        }

        return currentDateDir;
    }

    public static String getUploadFilePath(String filePath, LocalDate date){
        if(date == null || filePath == null){
            return "";
        }

        Integer year = date.getYear();
        Integer month = date.getMonthValue();
        Integer day = date.getDayOfMonth();

        String currentDateDir;
        if (isWindow()) {
            currentDateDir = String.format("%s\\%s\\%s\\%s\\", filePath, year, String.format("%02d", month), String.format("%02d", day));
        } else {
            currentDateDir = String.format("%s/%s/%s/%s/", filePath, year, String.format("%02d", month), String.format("%02d", day));
        }

        File dir = new File(currentDateDir);
        if(!dir.exists()){
            dir.mkdirs();
        }

        return currentDateDir;
    }

    public static boolean deleteFileByPath(String pathToFile) throws IOException {
        File file = new File( pathToFile );

        if( file.exists() && !file.isDirectory() ){
            Files.deleteIfExists(file.toPath());
            return true;
        }
        return false;
    }

    public static String readFileByPath(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        if(!Paths.get(filePath).toFile().exists()){
            return "";
        }
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }

    public static String readFile(File file) {
        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return contentBuilder.toString();
    }

    public static String readInputStream(InputStream file) {
        try {
            BufferedInputStream bis = new BufferedInputStream(file);
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int result = bis.read();
            while(result != -1) {
                buf.write((byte) result);
                result = bis.read();
            }
            // StandardCharsets.UTF_8.name() > JDK 7
            return buf.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "";
    }

    public static boolean isImage(String contentType){
        String type = contentType.split("/")[0];
        if(type.equals("image"))
            return true;
        else
            return false;
    }

    public static void packZip(File output, List<File> sources) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(output));
        zipOut.setLevel(Deflater.DEFAULT_COMPRESSION);

        for (File source : sources) {
            if (source.isDirectory()) {
                zipDir(zipOut, "", source);
            } else {
                zipFile(zipOut, "", source);
            }
        }
        zipOut.flush();
        zipOut.close();
    }

    private static void zipDir(ZipOutputStream zos, String path, File dir) throws IOException {
        if (!dir.canRead()) {
            System.out.println("Cannot read " + dir.getCanonicalPath() + " (maybe because of permissions)");
            return;
        }

        File[] files = dir.listFiles();
        path = buildPath(path, dir.getName());
        for (File source : files) {
            if (source.isDirectory()) {
                zipDir(zos, path, source);
            } else {
                zipFile(zos, path, source);
            }
        }
    }

    private static void zipFile(ZipOutputStream zos, String path, File file) throws IOException {
        if (!file.canRead()) {
            System.out.println("Cannot read " + file.getCanonicalPath() + " (maybe because of permissions)");
            return;
        }

        zos.putNextEntry(new ZipEntry(buildPath(path, file.getName())));

        FileInputStream fis = new FileInputStream(file);

        byte[] buffer = new byte[4092];
        int byteCount = 0;
        while ((byteCount = fis.read(buffer)) != -1) {
            zos.write(buffer, 0, byteCount);

        }
        fis.close();
        zos.closeEntry();
    }

    private static String buildPath(String path, String file) {
        if (path == null || path.isEmpty()) {
            return file;
        } else {
            return path + "/" + file;
        }
    }

//    public static void createQrImageFile(String temppath, String filename, String text) throws IOException {
//        if (temppath != null && !temppath.isEmpty()) {
//            File file = new File(temppath);
//            if (!file.exists()) {
//                file.mkdir();
//            }
//            QrCode qr = QrCode.encodeText(text, QrCode.Ecc.MEDIUM);  // Make the QR Code symbol
//            BufferedImage img = qr.toImageColor(5, 2);           // Convert to bitmap image
//            img= resizeImage(img);
//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//            ImageIO.write(img, "png", os);
//            try(OutputStream outputStream = new FileOutputStream(temppath+filename)) {
//                os.writeTo(outputStream);
//            }
//        }
//    }

    protected static BufferedImage resizeImage(BufferedImage originalImage) throws IOException {
        Image resultingImage = originalImage.getScaledInstance(90, 90, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(90, 90, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }


}
