#include "ofMain.h"
#include "ofApp.h"
#include "vsAdapter.h"
#include <locale>

//========================================================================
int main() { 
    setlocale(LC_ALL, "RUS");
    ofSetupOpenGL(1280, 720, OF_WINDOW);
    ofRunApp(new ofApp());
}
