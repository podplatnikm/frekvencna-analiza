package sample;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SampleController {

    public JFXButton gumbIzberiSifrirano;
    final FileChooser fcSifrirano = new FileChooser();
    public File fileSifrirano;

    public JFXButton gumbIzberiReferencno;
    final FileChooser fcReferencno = new FileChooser();
    public File fileReferencno;

    public Label labelIzberiSifrirano;
    public Label labelIzberiReferencno;

    public JFXButton gumbAnaliziraj;
    public Label labelAnalizirajNapaka;

    public AnchorPane poljeAnaliza;
    public JFXTextArea taBesedilo;
    public JFXListView listviewA;
    public JFXListView listviewB;
    public JFXCheckBox checkboxDollar;
    public JFXTextField tfZamenjajB;
    public JFXTextField tfZamenjajA;
    public Label imeShranjene;

    private String pot;

    public void izberiSifrirano(ActionEvent actionEvent) {
        fcSifrirano.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT datoteka", "*.txt")
        );
        fileSifrirano  = fcSifrirano.showOpenDialog(new Stage());
        pot = fileSifrirano.getParent();
        labelIzberiSifrirano.setText(fileSifrirano.getName());
    }

    public void izberiReferencno(ActionEvent actionEvent) throws IOException {
        fcReferencno.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT datoteka", "*.txt")
        );

        fileReferencno = fcReferencno.showOpenDialog(new Stage());
        labelIzberiReferencno.setText(fileReferencno.getName());

    }

    public void zazeniAnalizo(ActionEvent actionEvent) throws IOException {
        if(fileReferencno != null && fileSifrirano !=null){
            labelAnalizirajNapaka.setText("");
            poljeAnaliza.setDisable(false);
            checkboxDollar.setSelected(true);

            Analizator analizator = new Analizator();

            HashMap<Character, Integer> mapaSifrirano = analizator.analizirajDatoteko(fileSifrirano);
            HashMap<Character, Integer> mapaReferencno = analizator.analizirajDatoteko(fileReferencno);

            ArrayList<Par> listParovSifrirano = new ArrayList<>();
            ArrayList<Par> listParovReferencno = new ArrayList<>();

            Iterator it = mapaSifrirano.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry pair = (Map.Entry)it.next();
                listParovSifrirano.add(new Par((Character) pair.getKey(), (Integer) pair.getValue()));
                it.remove();
            }

            Iterator it2 = mapaReferencno.entrySet().iterator();
            while(it2.hasNext()){
                Map.Entry pair = (Map.Entry)it2.next();
                listParovReferencno.add((new Par((Character) pair.getKey(), (Integer) pair.getValue())));
                it2.remove();
            }

            listParovSifrirano.sort((o1, o2) -> o2.getI().compareTo(o1.getI()));
            listParovReferencno.sort((o1, o2) -> o2.getI().compareTo(o1.getI()));

            listviewA.setItems(FXCollections.observableArrayList(listParovSifrirano));

            listviewB.setItems(FXCollections.observableArrayList(listParovReferencno));

            /*Multiset<Character> mapaSifrirano = analizator.analizirajDatoteko(fileSifrirano);
            Multiset<Character> mapaReferencna = analizator.analizirajDatoteko(fileReferencno);

            Iterable<Multiset.Entry<Character>> mapaSifriranoSortirano = Multisets.copyHighestCountFirst(mapaSifrirano).entrySet();
            Iterable<Multiset.Entry<Character>> mapaReferencnaSortirano = Multisets.copyHighestCountFirst(mapaReferencna).entrySet();*/

            /*System.out.println("Sifrirano: ");
            System.out.println(Arrays.toString(listParovSifrirano.toArray()));
            System.out.println("*********");
            System.out.println("Referencno: ");
            System.out.println(Arrays.toString(listParovReferencno.toArray()));*/


            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileSifrirano));
            int ch;
            StringBuilder stringBuffer = new StringBuilder();

            while((ch = bufferedReader.read()) != -1){
                    char c = (char) ch;
                    stringBuffer.append("$").append(c);
            }

            String besediloSifrirano = stringBuffer.toString();

            int natancnost = 10;
            for(int i = 0; i < listParovSifrirano.size(); i++){
                if(listParovSifrirano.get(i).getI() > natancnost){
                    if(!Objects.equals(listParovSifrirano.get(i).getI(), listParovSifrirano.get(i + 1).getI())){
                        if(i != 0){
                            if(!Objects.equals(listParovSifrirano.get(i).getI(), listParovSifrirano.get(i - 1).getI())){
                                char tempS = listParovSifrirano.get(i).getC();
                                char tempR = listParovReferencno.get(i).getC();
                                besediloSifrirano = analizator.zamenjajCrki(besediloSifrirano, tempS, tempR);
                            }
                        }else{
                            besediloSifrirano = analizator.zamenjajCrki(besediloSifrirano, listParovSifrirano.get(0).getC(), listParovReferencno.get(0).getC());
                        }
                    }
                }else{
                    break;
                }
            }
            //besediloSifrirano = besediloSifrirano.replace("A", "U");
            //besediloSifrirano = besediloSifrirano.replace("&", "");
            taBesedilo.setText(besediloSifrirano);
        }else{
            labelAnalizirajNapaka.setText("NAPAKA: Izberite datoteke!");
            poljeAnaliza.setDisable(true);
        }
    }

    public void zamenjajCrke(ActionEvent actionEvent) {
        boolean samoNezamenjane = checkboxDollar.isSelected();

        if(samoNezamenjane){
            String besedilo = taBesedilo.getText();
            besedilo = besedilo.replace("$"+tfZamenjajA.getText().charAt(0), Character.toString(tfZamenjajB.getText().charAt(0)));
            taBesedilo.setText(besedilo);
        }else{
            String besedilo = taBesedilo.getText();
            besedilo = besedilo.replace(Character.toString(tfZamenjajA.getText().charAt(0)), Character.toString(tfZamenjajB.getText().charAt(0)));
            taBesedilo.setText(besedilo);
        }
    }

    public void shrani(ActionEvent actionEvent) throws IOException {
        FileWriter fileWriter = null;
        int id = ThreadLocalRandom.current().nextInt(1000, 9999 + 1);
        String imeDatoteke = "desifiranoBesedilo"+id+".txt";
        try {
            fileWriter = new FileWriter(new File(pot+"\\"+imeDatoteke), true);
            fileWriter.write(taBesedilo.getText());
            imeShranjene.setText("\""+ imeDatoteke +"\"");
        }catch (Exception e){
            System.out.println("NAPAKA: "+e.getMessage());
        }finally {
            if(fileWriter != null){
                fileWriter.close();
            }
            
        }
        
        

    }

    public void odstraniDolar(ActionEvent actionEvent) {
        taBesedilo.setText(taBesedilo.getText().replace("$", ""));
    }
}
