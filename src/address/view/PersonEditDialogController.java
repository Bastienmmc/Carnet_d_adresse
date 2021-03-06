package address.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import address.model.Person;


/**
 * The controller for Person Edit.
 * Dialog to edit details of a person.
 *
 * @author Sandra
 */
public class PersonEditDialogController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField adresseField;
    @FXML
    private TextField codePostalField;
    @FXML
    private TextField villeField;
    @FXML
    private TextField numField;
    @FXML
    private TextField num2Field;
    @FXML
    private TextField mailField;


    private Stage dialogStage;
    private Person person;
    private boolean okClicked = false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    /**
     * Sets the person to be edited in the dialog.
     *
     * @param person
     */
    public void setPerson(Person person) {
        this.person = person;

        nomField.setText(person.getNom());
        prenomField.setText(person.getPrenom());
        adresseField.setText(person.getAdresse());
        codePostalField.setText(person.getCodePostal());
        villeField.setText(person.getVille());
        numField.setText(person.getNum());
        num2Field.setText(person.getNum2());
        mailField.setText(person.getMail());
        
    }

     /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            person.setNom(nomField.getText());
            person.setPrenom(prenomField.getText());
            person.setAdresse(adresseField.getText());
            person.setCodePostal(codePostalField.getText());
            person.setVille(villeField.getText());
            person.setNum(numField.getText());
            person.setNum2(num2Field.getText());
            person.setMail(mailField.getText());            

            okClicked = true;
            dialogStage.close();
        }
    }

     /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Attention");
            alert.setHeaderText("Vos modifications ne seront pas sauvegardées");
            alert.setContentText("Etes vous sûr(e) de vouloir continuer ?");            
            alert.showAndWait();
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (nomField.getText() == null || nomField.getText().length() == 0) {
            errorMessage += "Nom invalide!\n";
        }
        if (adresseField.getText() == null || adresseField.getText().length() == 0) {
            errorMessage += "Adresse invalide!\n";
        }
        if (codePostalField.getText() == null || codePostalField.getText().length() == 0) {
            errorMessage += "Code postal invalide!\n";
        } 
        if (villeField.getText() == null || villeField.getText().length() == 0) {
            errorMessage += "Ville invalide!\n";
        }        
        if (numField.getText() == null || numField.getText().length() == 0) {
            errorMessage += "Téléphone invalide!\n";
        }        
        if (mailField.getText() == null || mailField.getText().length() == 0) {
            errorMessage += "E-mail invalide!\n";
        }  
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Champs invalides");
            alert.setHeaderText("Veuillez corriger les champs invalides.");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
    
}
