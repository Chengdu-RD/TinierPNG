package task;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import utils.CommonUtils;

import java.io.File;

public class PngQuantCompressor implements Compressor {
    @Override
    public long compress(Project project, VirtualFile file) {
        long preSize = file.getLength();
        String cmd = project.getBasePath() + File.separator + "libs" + File.separator + "pngquant" + CommonUtils.getCmdFileName("pngquant");
        boolean result = CommonUtils.executeCommand(cmd, "-v", "--force", "--speed=1", "--ext=.png", file.getName());
        return result ? preSize - file.getLength() : 0L;
    }

}
