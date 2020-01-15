//
//  vsAdapter.hpp
//  testOF3
//
//  Created by Konstantin Kimlaev on 09.01.2020.
//

#ifndef vsAdapter_h
#define vsAdapter_h

#include "ofMain.h"
#include "ofxUDPManager.h"

class vscListener{
public:
    void start_race();
    void finish_race();
};

class vscNetPackage{
public:
    int id;
    int camera_id;
    string channel;
    bool is_send = false;
};
class vscAdapter;

class CServerThread: public ofThread {
public:
    ofTimer timer;
    vscAdapter *adapter;
    void init(vscAdapter *adapter);
    void threadedFunction();
};

class vscAdapter{
public:
    string host = "127.0.0.1";
    string udpMessage = "";
    int port = 53000;
    bool isStarted = false;
    int port_for_output = 53005;
    long time = 0;
    long programmID = 0;
    long packageID = 10000;
    ofxUDPManager udpConnection;
    ofxUDPManager responseUdpConnection;
    vector<vscNetPackage> netPackages;
    vector<long> receivedMessages;
    void start_server();
    void stop_server();
    void init(int port);
    void pilot_detected(int pilotNum, string label, string channel);
    void sendMessage ( string msg );
    void checkUDPmessage();
    void addReceivedMessage(long id);
    bool messageIsReceived(long id);
};


#endif /* vsAdapter_hpp */
