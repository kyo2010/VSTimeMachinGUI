/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Utils.TTS.LANG;

/**
 *
 * @author kyo
 */
public class RusSpeachMessages extends ISpeachMessages{
  public String groupFinished(long groupIndex){
    return "����� ������ " + groupIndex + " ���������";
  };
  
  public String pilotIsChecked(String pilot){
    return pilot+" ������";
  }
  
  public String raceIsOverIn10sec(){
   return "����� ���������� ����� ������ ������";
  }
  
  public String connected(){
    return "���������";
  }
  
  public String disconnected(){
    return "�����������";
  }
  
  public String pilot(String name){
    return "����� "+name;
  }
}
