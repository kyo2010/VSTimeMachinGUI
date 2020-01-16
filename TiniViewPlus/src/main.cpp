#include "ofMain.h"
#include "ofApp.h"
#include "vsAdapter.h"
#include <locale>

//========================================================================
int main() {

#ifdef TARGET_WIN32
    SetConsoleCP(CP_UTF8);
    SetConsoleOutputCP(CP_UTF8);
    setlocale(LC_ALL, "Russian");
#endif


    ofSetupOpenGL(1280, 720, OF_WINDOW);
    ofRunApp(new ofApp());
}
   
