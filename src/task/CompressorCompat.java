package task;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class CompressorCompat {
    private Compressor mCompressor;

    CompressorCompat(@NotNull Compressor compressor) {
        mCompressor = compressor;
    }

    public void optimize(final Project project, final Collection<VirtualFile> files, final boolean showBalloon, boolean synchronous) {
        Task.Backgroundable task = new Task.Backgroundable(project, "Compress PNG Files", true) {
            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {

            }
        };

        if (synchronous) {
            ProgressManager.getInstance().runProcessWithProgressSynchronously(createOptimizeRunnable((ProgressIndicator) null, files, false, project), task.getTitle(), true, project);
        } else {
            ProgressManager.getInstance().run(task);
        }
    }

    private Runnable createOptimizeRunnable(ProgressIndicator progressIndicator, Collection<VirtualFile> files, boolean showBalloon, Project project) {
        return () -> {

        };
    }
}
