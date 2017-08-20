/**
 * Created by Ivan on 08.08.2017.
 */
public class RunMVC {

    //The order of instantiating the objects below will be important for some pairs of commands.
    //I haven't explored this in any detail, beyond that the order below works.


    public static void main(String[] args) {
        new RunMVC();
    }

    public RunMVC() {

        //create Model and View
        Model myModel = new Model();
        View myView = new View();
//        Thread Logic = new Thread(myModel);
//        Thread GUI = new Thread(myView);
        //tell Model about View.
        myModel.addObserver(myView);
        /*
            init model after view is instantiated and can show the status of the model
			(I later decided that only the controller should talk to the model
			and moved initialisation to the controller (see below).)
		*/
        //uncomment to directly initialise Model
        //myModel.setValue(start_value);

        //create Controller. tell it about Model and View, initialise model
        Controller myController = new Controller();
        myController.addModel(myModel);
        myController.addView(myView);
//        myController.initModel(start_value);
//        GUI.start();
//        Logic.start();
        //tell View about Controller
        myView.addController(myController);
        //and Model,
        //this was only needed when the view inits the model
        //myView.addModel(myModel);

    } //RunMVC()

}