package utils;

import com.intellij.openapi.vfs.VirtualFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommonUtils {
    /**
     * 判断文件是否是png
     *
     * @param file
     * @return
     */
    public static boolean isPngFile(VirtualFile file) {
        return file != null && !file.isDirectory() && "png".equalsIgnoreCase(file.getExtension());
    }

    public static boolean executeCommand(final String cmd, final String... params) {

        try {
            File cmdFile = new File(cmd);
            if (cmdFile.isFile()) {
                cmdFile.setExecutable(true, false);
                List<String> cmdList = new ArrayList<>();
                cmdList.add(cmd);
                cmdList.addAll(Arrays.asList(params));
                Process process = new ProcessBuilder(cmdList).redirectErrorStream(true).start();
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder error = new StringBuilder();
                String line;
                while (null != (line = br.readLine())) {
                    error.append(line);
                }
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    System.out.println(error.toString());
                }
                return exitCode == 0;
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e.toString());
            return false;
        }
        return false;
    }

}
