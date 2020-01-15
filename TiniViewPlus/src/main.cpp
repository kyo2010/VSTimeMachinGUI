#include "ofMain.h"
#include "ofApp.h"
#include "vsAdapter.h"
#include <locale>

//========================================================================
int main() { 
    //setlocale(LC_ALL, "RUS");
    //setlocale(LC_CTYPE, "rus");
    SetConsoleCP(CP_UTF8); 
    //SetConsoleOutputCP(CP_UTF8);
    //std::locale("rus_rus.1251");
    //SetConsoleCP(CP_UTF8);
    //SetConsoleOutputCP(CP_UTF8);
    SetConsoleOutputCP(CP_UTF8);
    setlocale(LC_ALL, "Russian");

    ofLogNotice() << "Запуск";
    wprintf(L"%s", L"Unicode -- English -- Русский -- Ελληνικά -- Español.\n");


    ofSetupOpenGL(1280, 720, OF_WINDOW);
    ofRunApp(new ofApp());
}
   