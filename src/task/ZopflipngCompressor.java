package task;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import utils.CommonUtils;

import java.io.File;

public class ZopflipngCompressor implements Compressor {
    @Override
    public long compress(Project project, VirtualFile file) {
        long preSize = file.getLength();
        String cmd = project.getBasePath() + File.separator + "libs" + File.separator + "zopfli" + CommonUtils.getCmdFileName("zopflipng");
        boolean result = CommonUtils.executeCommand(cmd, "-y", "-m", file.getName(), file.getName());
        return result ? preSize - file.getLength() : 0L;
    }
}
