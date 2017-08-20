import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Ivan on 08.08.2017.
 */
public class Controller {
    Controller controller;
    Model model;
    View view;

    public Controller() {
        this.controller = this;
    }

    public void addModel(Model m) {

//        System.out.println("Controller: adding model");
        this.model = m;
    } //addModel()

    public void addView(View v) {
//        System.out.println("Controller: adding view");
        this.view = v;
    } //addView()

//    public void initModel(int x){
//        model.getFiles();
//    } //initModel()

//    public class StartFindActivity implements ActionListener {
//        public void actionPerformed(ActionEvent e) {
//            model.MakeSearch(view.getAddr(), view.getFileFormat(), view.getDataToSearch());
//        }
//    }
//
//    public class OpenFileActivity implements ActionListener {
//        public void actionPerformed(ActionEvent e) {
//            model.openFile(view.getSelectedNode());
//        }
//    }


    public class TreeSelectionCheck implements TreeSelectionListener {
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            if (!model.isTabbedPaneOpen(view.getSelectedNode().toString()))
                view.addtabbedPane(view.getSelectedNode().toString(), model.openFile(view.getSelectedNode()), model.getTabbedPaneNum(view.getSelectedNode()), controller);

        }
    }

    public class CloseTabbedPane implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
//            model.closeTabbedPane(f);
        }
    }

    public class FileChooserListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            model.MakeSearch(view.chooseFile(), view.getFileFormat(), view.getDataToSearch());
        }
    }


    public class NextActivity implements ActionListener {
        public void actionPerformed(ActionEvent e) {
          ;
            model.nextFind(model.getFindResultByTabbedIndex(view.getTabbedPaneSelIndex()));
        }
    }

    public class PreviousActivity implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        }
    }

}
