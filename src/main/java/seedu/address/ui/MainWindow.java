package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ViewTab;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.misc.InfoPanelTypes;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.student.Student;
import seedu.address.ui.infopanel.InfoPanel;
import seedu.address.ui.infopanel.LessonInfoPanel;
import seedu.address.ui.infopanel.StudentInfoPanel;
import seedu.address.ui.listpanel.LessonListPanel;
import seedu.address.ui.listpanel.ListPanel;
import seedu.address.ui.listpanel.StudentListPanel;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private ListPanel lessonListPanel;
    private ListPanel personListPanel;
    //    private LessonListPanel lessonListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;
    private InfoPanel infoPanel;

    @FXML
    private StackPane commandBoxPlaceholder;
    @FXML
    private MenuItem helpMenuItem;
    @FXML
    private StackPane lessonListPanelPlaceholder;
    @FXML
    private StackPane personListPanelPlaceholder;
    @FXML
    private StackPane infoPanelPlaceholder;
    @FXML
    private StackPane resultDisplayPlaceholder;
    @FXML
    private StackPane statusbarPlaceholder;
    @FXML
    private TabPane listPane;
    @FXML
    private Tab studentTab;
    @FXML
    private Tab lessonTab;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {

        updateAndPopulateLessonList();
        updateAndPopulatePersonList();

        // Temporary lesson placeholder
        //Lesson tempLesson = logic.getFilteredLessonList().get(0);
        //tempPopulateInfoPanelWithLessonAndList(tempLesson, logic.getFilteredStudentList());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    private void updateAndPopulatePersonList() {
        populateListPanelWithStudents(logic.getFilteredStudentList());
        // Temporary person placeholder
        Student tempPerson = logic.getFilteredStudentList().get(0);
        // tempPopulateInfoPanelWithPersonAndList(tempPerson, logic.getFilteredLessonList());
    }

    private void updateAndPopulateLessonList() {
        populateListPanelWithLessons(logic.getFilteredLessonList());
        // Temporary lesson placeholder
        Lesson tempLesson = logic.getFilteredLessonList().get(0);
        // tempPopulateInfoPanelWithLessonAndList(tempLesson, logic.getFilteredPersonList());
    }

    /**
     * Toggles to the tab provided by the enum value.
     * @param toggleTo
     */
    public void toggleTab(ViewTab toggleTo) {
        if (toggleTo.equals(ViewTab.LESSON)) {
            toggleLessonTab();
        } else {
            toggleStudentTab();
        }
    }

    /**
     * Toggles to student tab.
     */
    public void toggleStudentTab() {
        updateAndPopulatePersonList();
        listPane.getSelectionModel().select(studentTab);
    }

    /**
     * Toggles to student tab.
     */
    public void toggleLessonTab() {
        updateAndPopulateLessonList();
        listPane.getSelectionModel().select(lessonTab);
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    /**
     * Updates the InfoPanel.
     */
    private void handleInfoPanelUpdate(InfoPanelTypes infoPanelTypes) {
        switch (infoPanelTypes) {
        case STUDENT:
            logger.info("INFO: Updating InfoPanel with selected student");
            Student selectedStudent = logic.getSelectedStudent();
            populateInfoPanelWithStudent(selectedStudent);
            break;
        case LESSON:
            logger.info("INFO: Updating InfoPanel with selected lesson");
            Lesson selectedLesson = logic.getSelectedLesson();
            populateInfoPanelWithLesson(selectedLesson);
            break;
        default:
            logger.severe("WARNING: Something went wrong with handling the InfoPanels");
        }
    }
    public ListPanel getLessonListPanel() {
        return lessonListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (!commandResult.toggleTo().equals(ViewTab.NONE)) {
                toggleTab(commandResult.toggleTo());
            }

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            if (commandResult.isUpdateInfoPanel()) {
                InfoPanelTypes infoPanelType = commandResult.getInfoPanelType();
                handleInfoPanelUpdate(infoPanelType);
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }

    private void populateListPanelWithLessons(ObservableList<Lesson> list) {
        lessonListPanel = new LessonListPanel(list);
        populateLessonListPanel(lessonListPanel);
    }

    private void populateLessonListPanel(ListPanel newListPanel) {
        lessonListPanelPlaceholder.getChildren().add(newListPanel.getRoot());
    }

    private void populateListPanelWithStudents(ObservableList<Student> list) {
        personListPanel = new StudentListPanel(list);
        populatePersonListPanel(personListPanel);
    }

    private void populatePersonListPanel(ListPanel newListPanel) {
        personListPanelPlaceholder.getChildren().add(newListPanel.getRoot());
    }


    private void populateInfoPanelWithStudent(Student selectedStudent) {
        infoPanel = new StudentInfoPanel(selectedStudent);
        StudentInfoPanel studentInfoPanel = (StudentInfoPanel) this.infoPanel;
        populateInfoPanel(infoPanelPlaceholder, studentInfoPanel);
    }

    private void populateInfoPanelWithLesson(Lesson selectedLesson) {
        infoPanel = new LessonInfoPanel(selectedLesson);
        LessonInfoPanel lessonInfoPanel = (LessonInfoPanel) this.infoPanel;
        populateInfoPanel(infoPanelPlaceholder, lessonInfoPanel);
    }

    private void populateInfoPanel(StackPane infoPanelPlaceholder, InfoPanel newInfoPanel) {
        infoPanelPlaceholder.getChildren().clear();
        infoPanelPlaceholder.getChildren().add(newInfoPanel.getRoot());
    }
}
