package task;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import utils.CommonUtils;

import java.util.*;

public class CompressorCompat {
    private Compressor mCompressor;

    CompressorCompat(@NotNull Compressor compressor) {
        mCompressor = compressor;
    }

    public void optimize(final Project project, final Collection<VirtualFile> files, final boolean showBalloon, boolean synchronous) {
        Task.Backgroundable task = new Task.Backgroundable(project, "Compress PNG Files", true) {
            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                createOptimizeRunnable(progressIndicator, files, showBalloon, project).run();
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
            long optimizedSize = 0L;
            int optmizedFiles = 0;
            LinkedList<VirtualFile> pngFiles = new LinkedList<>(files);
            while (!pngFiles.isEmpty()) {
                VirtualFile pngFile = pngFiles.pop();
                if (progressIndicator != null) {
                    progressIndicator.checkCanceled();
                    progressIndicator.setText(pngFile.getPath());
                }

                if (pngFile.isDirectory()) {
                    pngFile.getChildren();
                    pngFiles.addAll(Arrays.asList(pngFile.getChildren()));
                } else if (CommonUtils.isPngFile(pngFile)) {
                    long profit = mCompressor.compress(project, pngFile);
                    if (profit > 0) {
                        optimizedSize += profit;
                        optmizedFiles++;
                    }
                }
            }
            if (showBalloon) {
                String message = optmizedFiles + " files were optimized<br/>" + CommonUtils.formatByte(optimizedSize, true) + " saved";
                (new NotificationGroup("PNG Optimizer", NotificationDisplayType.BALLOON, true)).createNotification(message, NotificationType.INFORMATION).notify(project);
            }
        };
    }
}
