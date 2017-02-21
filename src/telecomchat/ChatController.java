package telecomchat;

import java.io.File;
import java.net.URL;
import java.util.*;

import de.dfki.csv.CSVReader;
import de.dfki.csv.Conversation;
import de.dfki.csv.ConversationLine;
import de.dfki.csv.metadata.MetaDataObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Robbie
 */
public class ChatController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ScrollPane screolPane;
    @FXML
    private Button openFileButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button previousButton;
    @FXML
    private ComboBox<String> sessionList;
    @FXML
    private Label sessionName;
    private int current_position = 0;

    private GridPane chatGridPane;

    private TelecomChat telecomChat;

    private ObservableList<String> sessionobservableList;

    private CSVReader csvReader;
    private HashMap<String, Conversation> conversations;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // TODO
        sessionList.setOnAction((event) -> {
            String s = sessionList.getSelectionModel().getSelectedItem();
            if (s != null) {
                // set the setValue of combobox
                setCurrentPosition();
                sessionName.setText(s);
                chatGridPane.getChildren().clear();
                String sessionId = sessionList.getSelectionModel().getSelectedItem();
                if (!sessionId.equals("")) {
                    Conversation conversation = conversations.get(sessionId);
                    int i = 0;
                    for (Object objectLine : conversation.getMessages()) {
                        ConversationLine line = (ConversationLine) objectLine;
                        addUserDialog(i, line.getUserQuestion(), line);
                        addSystemDialog(i + 1, line.getSystemResponse(), line);
                        i++;
                    }

                }

            }
        });

        openFileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sessionName.setText("openFileButton");
                handleOpen();
            }
        });

        nextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                moveNext();
                setCurrentConversation();
                conversations.keySet();
            }
        });

        previousButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                movePrevious();
            }
        });

        showChatOverview();
//        fillCombobox(list);
    }

    private void movePrevious() {
        if (current_position > 0) {
            current_position -= 1;
            sessionList.getSelectionModel().select(current_position);
        } else {
            previousButton.setDisable(true);
        }
    }

    private void setCurrentPosition() {
        current_position = sessionList.getSelectionModel().getSelectedIndex();
        if (nextButton.isDisable() && current_position < sessionobservableList.size()) {
            nextButton.setDisable(false);
        }
        if (previousButton.isDisable() && current_position > 0) {
            previousButton.setDisable(false);
        }
    }

    private void setCurrentConversation() {
    }

    private void moveNext() {
        if (current_position < sessionobservableList.size()) {
            current_position += 1;
            sessionList.getSelectionModel().select(current_position);
        } else {
            nextButton.setDisable(true);
        }
    }

    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(telecomChat.getPrimaryStage());
        if (file != null) {
            String filename = file.getAbsolutePath();
            csvReader = new CSVReader(filename, ',');
            csvReader.readFile();
            conversations = csvReader.parse();
            fillCombobox(conversations.keySet());
        }
    }

    public void setMainApp(TelecomChat telecomChat) {
        this.telecomChat = telecomChat;
    }

    public void fillCombobox(Collection<String> s) {
        sessionobservableList = FXCollections.observableArrayList(s);

        sessionList.getItems().clear();
        sessionList.getItems().addAll(sessionobservableList);

        sessionList.setValue(sessionobservableList.get(0));
        sessionName.setText(sessionobservableList.get(0));

    }

    private void showChatOverview() {

        chatGridPane = new GridPane();

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(100);
        chatGridPane.getColumnConstraints().add(c1);
        chatGridPane.setVgap(10);
        screolPane.setContent(chatGridPane);
    }

    private void addUserDialog(int i, String dialog, ConversationLine line) {
        if (!dialog.equals("")) {
            dialog = dialog + "\n\n" + line.getTimestamp().toString();
            Label chatMessage = new Label(dialog);
//            chatMessage.setTextAlignment(TextAlignment.RIGHT);
            chatMessage.setAlignment(Pos.TOP_LEFT);
            
            chatMessage.setWrapText(true);
            chatMessage.setPrefWidth(600);

            GridPane.setHalignment(chatMessage, HPos.LEFT);
            chatMessage.getStyleClass().add("message-bubble-left");

            chatGridPane.addRow(i, chatMessage);
        }
    }

    private void renderMetadata(ConversationLine line, Label chatMessage) {
        LinkedList<MetaDataObject> metadataObjects = line.getMetadataObjects();
        for (MetaDataObject object : metadataObjects) {
            object.render(chatMessage);
        }
    }

    private void addSystemDialog(int i, String dialog, ConversationLine line) {
        if (!dialog.equals("")) {
            dialog = dialog + "\n\n" + line.getTimestamp().toString();
            Label chatMessage = new Label(dialog);
//            chatMessage.setTextAlignment(TextAlignment.RIGHT);
chatMessage.setAlignment(Pos.TOP_LEFT);
            chatMessage.setWrapText(true);
            chatMessage.setPrefWidth(600);
            GridPane.setHalignment(chatMessage, HPos.RIGHT);
            chatMessage.getStyleClass().add("message-bubble-right");
            renderMetadata(line, chatMessage);
            chatGridPane.addRow(i, chatMessage);
        }
    }

}