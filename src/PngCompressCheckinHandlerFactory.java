import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.changes.CommitContext;
import com.intellij.openapi.vcs.changes.CommitExecutor;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory;
import com.intellij.openapi.vcs.ui.RefreshableOnComponent;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PairConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class PngCompressCheckinHandlerFactory extends CheckinHandlerFactory {
    @NotNull
    @Override
    public CheckinHandler createHandler(@NotNull CheckinProjectPanel checkinProjectPanel, @NotNull CommitContext commitContext) {
        CheckinHandler checkinHandler = new CheckinHandler() {
            @Nullable
            @Override
            public RefreshableOnComponent getBeforeCheckinConfigurationPanel() {
                return getCompressCheckBox(checkinProjectPanel);
            }

            @Override
            public ReturnResult beforeCheckin(@Nullable CommitExecutor executor, PairConsumer<Object, Object> additionalDataConsumer) {
                boolean isChecked = PropertiesComponent.getInstance().getBoolean("PNG_OPTIMIZER_STATE_KEY", true);
                if (isChecked) {
                    ArrayList<VirtualFile> pngFiles = getPngFiles(checkinProjectPanel);
                    if (!pngFiles.isEmpty()) {
                        LocalFileSystem.getInstance().refreshFiles(pngFiles);

                    }
                }

                return super.beforeCheckin(executor, additionalDataConsumer);
            }
        };
        return checkinHandler;
    }

    private RefreshableOnComponent getCompressCheckBox(@NotNull CheckinProjectPanel checkinProjectPanel) {
        final JCheckBox checkBox = new JCheckBox("Compress PNG files");
        return new RefreshableOnComponent() {
            @Override
            public JComponent getComponent() {
                JPanel root = new JPanel(new BorderLayout());
                if (!getPngFiles(checkinProjectPanel).isEmpty()) {
                    root.add(checkBox, "West");
                }

                return root;
            }

            @Override
            public void refresh() {

            }

            @Override
            public void saveState() {
                PropertiesComponent.getInstance().setValue("PNG_OPTIMIZER_STATE_KEY", checkBox.isSelected());
            }

            @Override
            public void restoreState() {
                checkBox.setSelected(PropertiesComponent.getInstance().getBoolean("PNG_OPTIMIZER_STATE_KEY", true));
            }
        };
    }

    @NotNull
    private ArrayList<VirtualFile> getPngFiles(@NotNull CheckinProjectPanel panel) {
        ArrayList<VirtualFile> pngFiles = new ArrayList<>();
        Iterator iterator = panel.getVirtualFiles().iterator();
        while (iterator.hasNext()) {
            VirtualFile file = (VirtualFile) iterator.next();
            if ("png".equalsIgnoreCase(file.getExtension())) {
                pngFiles.add(file);
            }
        }
        return pngFiles;
    }


}
