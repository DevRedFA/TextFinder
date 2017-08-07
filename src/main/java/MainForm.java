import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ivan on 30.07.2017.
 */
public class MainForm implements Runnable {
    private DefaultMutableTreeNode root;

    private DefaultTreeModel treeModel;

    private JTree tree;

    public void createTree() {
        DefaultMutableTreeNode top =
                new DefaultMutableTreeNode("The Java Series");

    }

    @Override
    public void run() {
        JFrame frame = new JFrame("File Browser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        File fileRoot = new File("D:\\test");
        root = new DefaultMutableTreeNode(new MainForm.FileNode(fileRoot));
        treeModel = new DefaultTreeModel(root);

        tree = new JTree(treeModel);
        tree.setShowsRootHandles(true);
        JScrollPane scrollPane = new JScrollPane(tree);

        frame.add(scrollPane);
        frame.setLocationByPlatform(true);
        frame.setSize(640, 480);
        frame.setVisible(true);

        MainForm.CreateChildNodes ccn =
                new MainForm.CreateChildNodes(fileRoot, root);
        new Thread(ccn).start();
    }

    public class CreateChildNodes implements Runnable {

        private DefaultMutableTreeNode root;

        private File fileRoot;

        public CreateChildNodes(File fileRoot,
                                DefaultMutableTreeNode root) {
            this.fileRoot = fileRoot;
            this.root = root;
        }

        public void addFileToNode(File f, DefaultMutableTreeNode node) {
            List<File> filo = new ArrayList<>();
            filo.add(f);
            File g = f.getParentFile();
            while (!g.equals(fileRoot)) {
                filo.add(g);
                g = g.getParentFile();
            }
            addNode(filo, node);
            return;
        }

        public void addNode(List<File> list, DefaultMutableTreeNode node) {
            if (list.isEmpty()) {
                return;
            }
            List<DefaultMutableTreeNode> listOfChildren = Collections.list(node.children());
            File file = list.remove(list.size() - 1);
            for (DefaultMutableTreeNode childNode1 : listOfChildren) {
                if (file.toString().endsWith(childNode1.toString())) {
                    addNode(list, childNode1);
                    return;
                }
            }
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new FileNode(file));
            node.add(childNode);
            addNode(list, childNode);
        }

        @Override
        public void run() {
            createChildren(fileRoot, root);
        }

        private void createChildren(File fileRoot,
                                    DefaultMutableTreeNode node) {
//            File[] files = fileRoot.listFiles();
//            if (files == null) return;

            List<File> filesList = Main.getFiles(fileRoot.getAbsolutePath(), ".log");

            if (filesList.isEmpty()) return;
            for (File f : filesList) {
                try {
                    if (Main.isFileContains("Passed", f)) {
                        addFileToNode(f, node);
//                        node.add(new DefaultMutableTreeNode(new FileNode(f)));
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

//            for (File file : files) {
//                DefaultMutableTreeNode childNode =
//                        new DefaultMutableTreeNode(new FileNode(file));
//                node.add(childNode);
//                if (file.isDirectory()) {
//                    createChildren(file, childNode);
//                }
//            }
        }

    }

    public class FileNode {

        private File file;

        public FileNode(File file) {
            this.file = file;
        }

        @Override
        public String toString() {
            String name = file.getName();
            if (name.equals("")) {
                return file.getAbsolutePath();
            } else {
                return name;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MainForm());
    }

}






