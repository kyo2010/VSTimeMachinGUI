/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Utils.TTS.LANG;

import java.util.List;

/**
 *
 * @author kyo
 */
public class RusSpeachMessages extends ISpeachMessages{
  public SpeekText groupFinished(long groupIndex){
    return new SpeekText("����� ������ " + groupIndex + " ���������",2000);
  };
  
  public SpeekText pilotIsChecked(String pilot){
    return new SpeekText(pilot+" ������");
  }
  
  public SpeekText raceIsOverIn10sec(){
   return new SpeekText("����� ���������� ����� ������ ������",2000);
  }
  
  public SpeekText connected(){
    return new SpeekText("���������");
  }
  
  public SpeekText disconnected(){
    return new SpeekText("�����������");
  }
  
  public SpeekText pilot(String name){
    return new SpeekText("����� "+name,3000);
  }
  
  public SpeekText startMessage(String id){
    if (id.equalsIgnoreCase("one")) return new SpeekText("����",350);
    if (id.equalsIgnoreCase("two")) return new SpeekText("���",350);
    if (id.equalsIgnoreCase("three")) return new SpeekText("���",350);
    if (id.equalsIgnoreCase("attention")) return new SpeekText("��������, ����� ����� ��������� �������",5000,5000);
    return new SpeekText(id);      
  }
  
   public SpeekText lapTime(String pilot, int lap, int countLaps) {
    String krug = "";
    if (lap == countLaps) {
      krug = " �����";
    } else {
      krug =  " ���� "+lap;
    }
    /*return new SpeekText(pilot +" "+krug);*/
    if (lap==0) return new SpeekText(pilot + " �����");
    if (lap>countLaps) return new SpeekText(pilot);
    return new SpeekText(pilot +krug);
  }
  
  public String krugNumber(int lap) {
    if (lap == 1) {
      return "������";
    }
    if (lap == 2) {
      return "������";
    }
    if (lap == 3) {
      return "������";
    }
    if (lap == 4) {
      return "���������";
    }
    if (lap == 5) {
      return "�����";
    }
    if (lap == 6) {
      return "������";
    }
    if (lap == 7) {
      return "�������";
    }
    if (lap == 8) {
      return "�������";
    }
    if (lap == 9) {
      return "�������";
    }
    return "" + lap;
  }
  
   public SpeekText invatieGroup(long num, List<String> pilots){
    String text = "������������ ������ "+num+". ";
    for (String pilot :  pilots){
      text += pilot+"...";      
    }
    return new SpeekText(text, 1000+1000*pilots.size());
  }
   
   public SpeekText findTransponders(long num){
    String text = "��������, ������ "+num+". ���� ����� �������������. ��������� ������� ����� ������.";    
    return new SpeekText(text, 2000);
  }
}
