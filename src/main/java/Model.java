import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Ivan on 08.08.2017.
 */
public class Model extends java.util.Observable {
    private static final Logger logger = LoggerFactory.getLogger(Main.class); //LoggerOne
    private static int lastTabbedPane = 0;
    private List<FindResultNode> findResultNodes;
    private File fileRoot;

    Model() {
        findResultNodes = new LinkedList<>();
    }

//
//    SwingWorker worker = new SwingWorker<TreeModel, Integer>() {
//        @Override
//        protected TreeModel doInBackground() throws Exception {
//            // Background work
//
//            // Value transmitted to done()
//            return treeModel;
//        }
//
//        @Override
//        protected void process(List<Integer> chunks) {
//            // Process results
//        }
//
//        @Override
//        protected void done() {
//            // Finish sequence
//        }
//    };


//    @Override
//    public void notifyObservers(Object arg) {
//        super.notifyObservers(arg);
//    }

    //
    void closeTabbedPane(FindResultNode f) {
        findResultNodes.remove(f);
    }

    private String getTextFrom(File f, int Pos) {
        String string = null;
        try (RandomAccessFile accessFile = new RandomAccessFile(f, "r")) {
            accessFile.seek(Pos);
            string = accessFile.readLine();
        } catch (FileNotFoundException e) {
            logger.error("selected file {} not found", f.toString());
        } catch (IOException e) {
            logger.error("cant get {} file position", Pos);
        }
        return string;
    }

    String openFile(File f) {
        for (FindResultNode findResultNode : findResultNodes)
            if (findResultNode.getFile().equals(f)) {
                findResultNode.initPosition();
                return nextFind(findResultNode);
            }
        return null;
    }

    public String nextFind(FindResultNode findResultNode) {
        return getTextFrom(findResultNode.getFile(), findResultNode.getCurrentViewed());
    }

//    String previousFind(File f) {
//        List<Integer> findedPos = mapVar.get(f);
//        int currentPos = findedPos.indexOf(mapPos.get(f));
//        mapPos.put(f, findedPos.get(currentPos - 1));
//        return getTextFrom(f, currentPos);
//    }

    void MakeSearch(File addr, String fileFormat, String dataToSearch) {
        List<File> filesList = getFiles(addr, fileFormat);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new FindResultNode(addr, new LinkedList<>()));
        TreeModel treeModel = new DefaultTreeModel(root);
        fileRoot = addr;
        if (filesList.isEmpty()) return;
        for (File f : filesList) {
            List<Integer> list;
            if (!(list = FindRepeats(dataToSearch, f)).isEmpty()) {
                addFileToNode(f, root);
                findResultNodes.add(new FindResultNode(f, list));
            }
        }
        setChanged();
        notifyObservers(treeModel);
    } //MakeSearch()

    private List<Integer> FindRepeats(String s, File f) {
        logger.info("searching: \"{}\" in file {}", s, f);
        List<Integer> nFinds = new LinkedList<>();

//        looks cool, bad working with big files:
//        long count = Files.lines(f.toPath())
//                .filter(q -> q.contains(s))
//                .count();
        int size = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if ((line.contains(s))) {
                    nFinds.add(size);
                }
                size += line.length();
            }
        } catch (IOException e) {
            logger.error("Problems while opening: {}", f, e);
        }
        logger.info("finded {} entry", size);
        return nFinds;
    } //FindRepeats()

    private List<File> getFiles(File addr, String fileFormat) { // Список всех файлов заданного формата по данному адресу
        List<File> list = new ArrayList<>();
        try {
            logger.info("Trying open addr: {}", addr);
            list = Files.walk(addr.toPath())
                    .filter(Files::isRegularFile)
                    .filter(s -> s.toString().endsWith(fileFormat))
                    .map(Path::toFile)
                    .collect(Collectors.toList());
            logger.info("list with files correctly created");
        } catch (IOException e) {
            logger.error("wrong addr: {}, \n {}", addr, e);
        }
        return list;
    } //getFiles()

    private void addNode(List<File> list, DefaultMutableTreeNode node) {
        if (list.isEmpty()) {
            return;
        }
        ArrayList<DefaultMutableTreeNode> listOfChildren = Collections.list(node.children());
        File file = list.remove(list.size() - 1);
        for (DefaultMutableTreeNode childNode1 : listOfChildren) {
            if (file.toString().endsWith(childNode1.toString())) {
                addNode(list, childNode1);
                return;
            }
        }
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new FindResultNode(file, new LinkedList<>()));
        node.add(childNode);
        addNode(list, childNode);
    } //addNode()

    private void addFileToNode(File f, DefaultMutableTreeNode node) {
        List<File> filo = new ArrayList<>();
        filo.add(f);
        File g = f.getParentFile();
        while (!g.equals(fileRoot)) {
            filo.add(g);
            g = g.getParentFile();
        }
        addNode(filo, node);
    } //addFileToNode()

    boolean isTabbedPaneOpen(String s) {
        for (FindResultNode findResultNode : findResultNodes)
            if (findResultNode.getFile().toString().equals(s)) if (findResultNode.getnTabbedPane() != -1) return true;
        return false;
    } //isTabbedPaneOpen()

    public List<FindResultNode> getFindResultNodes() {
        return findResultNodes;
    } //getFindResultNodes()

    int getTabbedPaneNum(File f) {
        for (FindResultNode findResultNode : findResultNodes)
            if (findResultNode.getFile().equals(f)) return findResultNode.getnTabbedPane();
        return -1;
    }

    public FindResultNode getFindResultByTabbedIndex(int tabbedPaneSelIndex) {
        for (FindResultNode findResultNode: findResultNodes)
            if (findResultNode.getnTabbedPane()==tabbedPaneSelIndex) return findResultNode;
        return null;
    }

    public class FindResultNode {
        private File file;
        private List<Integer> nFinded;
        private int nTabbedPane = -1;
        private int currentViewed = -1;

        FindResultNode(File file, List<Integer> nFinded) {
            this.file = file;
            this.nFinded = nFinded;
        }

        File getFile() {
            return file;
        }

        public List<Integer> getnFinded() {
            return nFinded;
        }

        public int getnTabbedPane() {
            return nTabbedPane;
        }

        int getCurrentViewed() {
            return currentViewed;
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

        void initPosition() {
            currentViewed = nFinded.get(0);
            nTabbedPane = lastTabbedPane++;
        }
    }

}
