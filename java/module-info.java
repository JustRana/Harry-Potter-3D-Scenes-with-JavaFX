// module project_cs451 {
//     requires javafx.controls;
//     requires javafx.fxml;

//     opens project_cs451 to javafx.fxml;
//     exports project_cs451;
// }

module project_cs451 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;
    requires javafx.graphics;

    opens project_cs451 to javafx.fxml;
    exports project_cs451;
}


