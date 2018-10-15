package utils;

import com.intellij.openapi.vfs.VirtualFile;

public class CommonUtils {
    /**
     * 判断文件是否是png
     * @param file
     * @return
     */
    public static boolean isPngFile(VirtualFile file){
        return file!=null && !file.isDirectory() && "png".equalsIgnoreCase(file.getExtension());
    }

}
