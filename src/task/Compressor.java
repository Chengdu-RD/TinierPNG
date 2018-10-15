package task;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public interface Compressor {
    /**
     * 压缩文件核心算法
     *
     * @param file 文件路径
     * @return 压缩大小 单位：byte
     */
    long compress(Project project, VirtualFile file);
}
