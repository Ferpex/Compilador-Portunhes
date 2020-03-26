
package portunhes;

import com.jfoenix.controls.JFXTextArea;
import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;
import portunhes.analisador.AnalisadorLexico;

public class FXMLTelaController implements Initializable
{
    private boolean codechanged;
    private File file;
    private String dialogResult;
    private StackPane stackpane;
    private VirtualizedScrollPane virtscrollpane;
    
    @FXML
    private BorderPane painel;
    @FXML
    private TextArea txLog;
    @FXML
    private Tab tabPrograma;
    @FXML
    private Tab tabLog;
    @FXML
    private CodeArea codearea;
    @FXML
    private Button btCompilar;
    @FXML
    private Button btLimpar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        initializeCodeArea();
    } 

    private void initializeCodeArea() {
        codearea = new CodeArea();
        codearea.setParagraphGraphicFactory(LineNumberFactory.get(codearea));

        Subscription cleanupWhenNoLongerNeedIt = codearea
                .multiPlainChanges()
                .successionEnds(Duration.ofMillis(50))
                .subscribe(ignore -> codearea.setStyleSpans(0, computeHighlighting(codearea.getText())));

       
        virtscrollpane = new VirtualizedScrollPane(codearea);
        stackpane = new StackPane(virtscrollpane);
        codearea.setOnKeyTyped(codeAreaKeyTyped());
        tabPrograma.setContent(stackpane);
    }
    private EventHandler<KeyEvent> codeAreaKeyTyped() {
        return (KeyEvent event) -> {

            if (event.getCharacter().equals("\t")) {
                codearea.replaceText(codearea.getCaretPosition() - 1, codearea.getCaretPosition(), "    ");
            }
        };
    }

    private static final String[] KEYWORDS = {"inicio()"},
            KEYTYPES = {"inteiro", "quebrado", "palavra","caracter", "boleano"},
            KEYCOMMAND = {"se", "enquanto", "para"};

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String TYPES_PATTERN = "\\b(" + String.join("|", KEYTYPES) + ")\\b";
    private static final String COMMAND_PATTERN = "\\b(" + String.join("|", KEYCOMMAND) + ")\\b";
    private static final String DECIMAL_PATTERN = "[0-9]+,[0-9]+";
    private static final String NUMBER_PATTERN = "[0-9]+";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BOOLEAN_PATTERN = "(false)|(true)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\\'[a-zA-ZçÇ]+\\'";
    private static final String OPERATOR_PATTERN = "\\+|\\-|\\*|\\/";
    private static final String LOGIC_PATTERN = "\\<\\=|\\>\\=|\\=\\=|\\!=|\\<|\\>";
    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ')' 
            + "|(?<TYPE>" + TYPES_PATTERN + ')' 
            + "|(?<COMMAND>" + COMMAND_PATTERN + ')' 
            + "|(?<PAREN>" + PAREN_PATTERN + ')'
            + "|(?<BRACE>" + BRACE_PATTERN + ')'
            + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ')'
            + "|(?<DECIMAL>" + DECIMAL_PATTERN + ')'
            + "|(?<NUMBER>" + NUMBER_PATTERN + ')'
            + "|(?<LOGIC>" + LOGIC_PATTERN + ')'
            + "|(?<OPERATOR>" + OPERATOR_PATTERN + ')'
            + "|(?<BOOLEAN>" + BOOLEAN_PATTERN + ')'
            + "|(?<STRING>" + STRING_PATTERN + ')'
    );

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass
                    = matcher.group("KEYWORD") != null ? "keyword"
                    : matcher.group("TYPE") != null ? "type"
                    : matcher.group("COMMAND") != null ? "command"
                    : matcher.group("DECIMAL") != null ? "decimal"
                    : matcher.group("NUMBER") != null ? "number"
                    : matcher.group("PAREN") != null ? "paren"
                    : matcher.group("BRACE") != null ? "brace"
                    : matcher.group("SEMICOLON") != null ? "semicolon"
                    : matcher.group("LOGIC") != null ? "logic"
                    : matcher.group("OPERATOR") != null ? "operator"
                    : matcher.group("BOOLEAN") != null ? "boolean"
                    : matcher.group("STRING") != null ? "string"
                    : null;

            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    @FXML
    private void clkApagar(ActionEvent event)
    {
        codearea.clear();
    }

    @FXML
    private void clkCompilar(ActionEvent event)
    {
        if(!codearea.getText().isEmpty())
        {
            AnalisadorLexico analisadorlexico = new AnalisadorLexico();
            txLog.setText(analisadorlexico.compilar(codearea.getText()));
        }
        else
        {
           txLog.setText("Não existe programa para compilar"); 
        }
    }
    
}
