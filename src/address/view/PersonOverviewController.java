package address.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import address.MainApp;
import address.model.Person;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;


/**
 * The controller for Person Overview. Manages the entry. Start with the last saved file 
 * and displays details of registered contacts. Allows to create, delete and edit the address book.
 * 
 * @author Sandra, Tian, Bastien
 */
public class PersonOverviewController {

    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> nomColumn;
    @FXML
    private TableColumn<Person, String> prenomColumn;
    @FXML
    private TextField filterField;

    @FXML
    private Label nomLabel;
    @FXML
    private Label prenomLabel;
    @FXML
    private Label adresseLabel;
    @FXML
    private Label codePostalLabel;
    @FXML
    private Label villeLabel;
    @FXML
    private Label numLabel;
    @FXML
    private Label num2Label;
    @FXML
    private Label mailLabel;
    
   

    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor is called before the initialize()
     * method.
     */
    public PersonOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        nomColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        prenomColumn.setCellValueFactory(cellData -> cellData.getValue().prenomProperty());

        // Clear person details.
        showPersonDetails(null);

        // Listen for selection changes and show the person details when changed.
        personTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));
        
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        
        // Add observable list data to the table
        personTable.setItems(filterEngine(mainApp.getPersonData()));
    }
    
    /**
     * Filters in real time the table view content when writing in the research bar
     * 
     * @param data
     * @return 
     */    
    private SortedList<Person> filterEngine(ObservableList<Person> data) {
        FilteredList<Person> filteredData = new FilteredList<>(data, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(myObject -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name field in your object with filter.
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(myObject.getNom()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                    // Filter matches first name.

                } else if (String.valueOf(myObject.getPrenom()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } 

                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Person> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(personTable.comparatorProperty());
        
        return sortedData;
    }
    
    /**
     * Fills all text fields to show details about the person. If the specified
     * person is null, all text fields are cleared.
     *
     * @param person the person or null
     */
    private void showPersonDetails(Person person) {
        if (person != null) {
            // Fill the labels with info from the person object.
            nomLabel.setText(person.getNom());
            prenomLabel.setText(person.getPrenom());
            adresseLabel.setText(person.getAdresse());
            codePostalLabel.setText(person.getCodePostal());
            villeLabel.setText(person.getVille());
            numLabel.setText(person.getNum());
            num2Label.setText(person.getNum2());
            mailLabel.setText(person.getMail());

            // birthdayLabel.setText(...);
        } else {
            // Person is null, remove all the text.
            nomLabel.setText("");
            prenomLabel.setText("");
            adresseLabel.setText("");
            codePostalLabel.setText("");
            villeLabel.setText("");
            numLabel.setText("");
            num2Label.setText("");
            mailLabel.setText("");

        }
    }

    /**
     * Called when the user clicks on the delete button.
     * 
     */
    @FXML
    private void handleDeletePerson() {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {            
            
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Attention");
            alert.setHeaderText("Vous vous apprêtez à supprimer cette entrée");
            alert.setContentText("Etes vous sûr(e) de vouloir continuer ?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                mainApp.getPersonData().remove(selectedIndex);
                //personTable.getItems().remove(selectedIndex);
                //personTable.getItems().remove(selectedIndex);
                //personTable.setItems(filterEngine(mainApp.getPersonData()));
            } else {
                alert.close();
            }
            
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Pas de selection");
            alert.setHeaderText("Aucune entrée selectionnée.");
            alert.setContentText("Veuillez selectionner une personne.");

            alert.showAndWait();
        }
    }

    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new person.
     */
    @FXML
    private void handleNewPerson() {
        Person tempPerson = new Person();
        boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
        if (okClicked) {
            mainApp.getPersonData().add(tempPerson);
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected person.
     */
    @FXML
    private void handleEditPerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
            if (okClicked) {
                showPersonDetails(selectedPerson);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Pas de selection");
            alert.setHeaderText("Aucune entrée selectionnée.");
            alert.setContentText("Veuillez selectionner une personne.");

            alert.showAndWait();
        }
    }

}
