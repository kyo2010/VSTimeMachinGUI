//
//  vsAdapter.cpp
//  testOF3
//
//  Created by Konstantin Kimlaev on 09.01.2020.
//

#include "vsAdapter.h"
#include "ofMain.h"
#include <sstream>
#include <regex>
#include "ofApp.h"

static CServerThread udp_thread;

class UDPCommand{
public:
    string command;
    vector<string> params;
    void init(const string& value){
        int pos = value.find("\r");
        string val = value;
        if (pos>0){
          val = val.substr(0,pos);
        };
        pos = val.find(":");
        if (pos>0){
            command = val.substr(0,pos);
            val = val.substr(pos+1);
          params.clear();
          std::string token;
          std::istringstream tokenStream(val);
          while (getline(tokenStream, token, ','))
          {
            params.push_back(token);
          }
        }else{
          command = val;
        }
    };
    string getParam(int index){
        if (index<params.size()) return params.at(index);
        return "";
    }
    long getLongParam(int index){
        if (index<params.size()) return stol(params.at(index));
        return -1;
    }
    string toString(){
        string res = "command: '"+command+"'\n";
        res+= to_string(params.size())+" params:";
        for (int i=0;i<params.size(); i++){
            res += "'"+params[i]+"'";
            res += ", ";
        }
        return res;
    }
};

//--------------------------------------------------------------
//  vcsListener
//--------------------------------------------------------------
void vscListener::start_race(){
    
};

void vscListener::finish_race(){
    
};

//--------------------------------------------------------------
//  vcsAdapter
//--------------------------------------------------------------
void vscAdapter::start_server(){
    ofLogNotice() << "start UDP server " << host << ":" << port;
    
    udpConnection.Create();
    udpConnection.Connect(host.c_str() ,port);
    udpConnection.SetNonBlocking(true);
    
    responseUdpConnection.Create();
    responseUdpConnection.SetEnableBroadcast(true);
    responseUdpConnection.Bind(this->port_for_output);
    responseUdpConnection.SetNonBlocking(true);
    
    isStarted = true;
    
    udp_thread.init(this);
};

void vscAdapter::sendMessage (string msg ){
    string msg1 = msg+"\r\n";
    udpConnection.Send(msg1.c_str(), msg1.length());
    if (!regex_search(msg1, regex("ping"))){
      ofLogNotice() << "snd:" << msg;
    }
}

void vscAdapter::stop_server(){
    if (isStarted){
      isStarted = false;
      udp_thread.stopThread();
      udpConnection.Close();
    }
};

void vscAdapter::init(int port_param){
    if (port_param!=-1) port = port_param;
};

void vscAdapter::pilot_detected(int pilotNum, string label){
    ofLogNotice() << "pilot detected:" << pilotNum << " " << label;
    vscNetPackage pack;
    pack.camera_id = pilotNum;
    pack.channel = label;
    pack.id = this->packageID;
    this->packageID++;
    netPackages.push_back(pack);
};

void vscAdapter::addReceivedMessage(long msgID){
    receivedMessages.push_back(msgID);
};
bool vscAdapter::messageIsReceived(long msgID){
    // todo Clear old message id
    if (receivedMessages.size()>100){
        receivedMessages.erase(receivedMessages.begin(), receivedMessages.begin()+50);
    }
    for (long i = 0; i<receivedMessages.size(); i++){
        if (receivedMessages[i]==msgID) return true;
    }
    return false;
};

void vscAdapter::checkUDPmessage(){
    if (!isStarted) return;
    char udpMessage[100000];
    responseUdpConnection.Receive(udpMessage,100000);
    this->udpMessage=udpMessage;
    if (this->udpMessage!=""){
      
        UDPCommand command;
        command.init(this->udpMessage);
        
        ofLogNotice() << "rcv:" << this->udpMessage;
        //ofLogNotice() << "udp command : \n" << command.toString();
        
        string message = this->udpMessage;
        
        if (command.command=="lapreceived"){
          long packgID = command.getLongParam(0);
          for (int i = 0; i < netPackages.size(); i++) {
             if (netPackages[i].id==packgID){
               netPackages.erase(netPackages.begin() + i);
               break;
              }
           }
        }else if (command.command=="invatieStart"){
            long msgID = command.getLongParam(0);
                if (!messageIsReceived(msgID)){
                    addReceivedMessage(msgID);
                    invitatinStrat();
                    if (!isEnglish()) {
                        setGroupCaption( command.getParam(1) + " : приглашение пилотов");
                    } else {
                        setGroupCaption(command.getParam(1) +" : pilots invitation");
                        // speakAny(EN_LANG , "Start");
                    }
                }
        }else if (command.command=="invatePilot"){
        // 0 - msgID
        // 1 - channel
        // 2 - Pilot Name
        // 3 - Pilot Url
        // 4 - color name
           long msgID = command.getLongParam(0);
           if (!messageIsReceived(msgID)){
              addReceivedMessage(msgID);
            invitationPilot(command.getParam(1),command.getParam(2),command.getParam(3),command.getParam(4));
           }
        }else if (command.command=="runRace"){
            setCountLapsForRace(command.getLongParam(2));
            if (!isEnglish()) {
                //setOverlayMessage("СТАРТ !");
                setGroupCaption(command.getParam(1) + " : старт через 3 секунды");
            } else {
                //setOverlayMessage("START !");
                setGroupCaption(command.getParam(1) +" : start in 3 seconds");
                // speakAny(EN_LANG , "Start");
            }
        }else if (command.command=="startRace"){
            setCountLapsForRace(command.getLongParam(2));
            if (!isEnglish()) {
               setOverlayMessage("СТАРТ !");
               setGroupCaption(command.getParam(1) + " : идёт гонка");
            } else {
               setOverlayMessage("START !");
               setGroupCaption(command.getParam(1) +" : race is started");
               // speakAny(EN_LANG , "Start");
            }
        }else if (command.command=="finishRace"){
            if (!isEnglish()) {
                setOverlayMessage("Гонка завершена !");
                setGroupCaption(command.getParam(1) + " : гонка завершена");
            } else {
                setOverlayMessage("STOP !");
                setGroupCaption(command.getParam(1) +" : race is finished");
                // speakAny(EN_LANG , "Start");
            }
        }else if (command.command=="addLap"){
          // 0 - msgID
          // 1 - channel
          // 2 - pilotName
          // 3 - lap
          // 4 - lap time long
          // 5 - lap time string
          // 6 - time form start
          // 7 - race time
          // void addLap(string channel, string name, int lap, long lapTime, long timeFromStart, long raceTime);
            addLap(command.getParam(1),command.getParam(2),command.getLongParam(3),command.getLongParam(4),command.getLongParam(6),command.getLongParam(7));
        }
        
    }
}

void CServerThread::init(vscAdapter *adapter){
    //timer.setPeriodicEvent(1000000000); // this is 1 second in nanoseconds
    timer.setPeriodicEvent(333000000); // this is 0.1 second in nanoseconds
    this->adapter =adapter;
    startThread();
};

void CServerThread::threadedFunction() {
        int loop = 0;
        while(isThreadRunning()) {
            timer.waitNext();
            if (!adapter->isStarted) break;
            // send package
            for (int i = 0; i < adapter->netPackages.size(); i++) {
              adapter->sendMessage("lap:channel"+adapter->netPackages[i].channel+"," + to_string(adapter->programmID)+","+to_string(adapter->netPackages[i].camera_id)
                  +","+to_string(adapter->netPackages[i].id)
                  +","+adapter->netPackages[i].channel);
            }
            
            loop++;
            if (loop>=3){
                long time = ofGetSystemTimeMillis();
                adapter->sendMessage("ping:" + to_string(adapter->programmID)+","+to_string(time));
                
                loop = 0;
            }
        }
    }

