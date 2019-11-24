package iai.kit.edu.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Mohammad
 * this class converts chromosome lists between master slave
 * and island model.
 *
 */
public class ChromosomeListConverter {
	
	
	public String convertChromosomeListToMasterSlave(File file) {
		//TODO: do the actual convertion
		String chromosomeList = "5 0 1\n" + 
				"0 0 20\n" + 
				"1  -3.081785\n" + 
				"2  1.280655\n" + 
				"3  -3.6397\n" + 
				"4  2.923765\n" + 
				"5  -2.511703\n" + 
				"6  -4.651066\n" + 
				"7  3.692258\n" + 
				"8  3.663588\n" + 
				"9  -2.927348\n" + 
				"10  1.066706\n" + 
				"11  1.02424\n" + 
				"12  2.33218\n" + 
				"13  0.1708629\n" + 
				"14  -0.7554981\n" + 
				"15  1.408773\n" + 
				"16  -3.536084\n" + 
				"17  -1.349556\n" + 
				"18  -4.387458\n" + 
				"19  5.057491\n" + 
				"20  -4.987598\n" + 
				"1 0 20\n" + 
				"1  4.509229\n" + 
				"2  3.055316\n" + 
				"3  -0.07905008\n" + 
				"4  -3.202004\n" + 
				"5  -2.55989\n" + 
				"6  1.322683\n" + 
				"7  -1.702373\n" + 
				"8  2.947244\n" + 
				"9  4.059527\n" + 
				"10  2.903412\n" + 
				"11  4.543773\n" + 
				"12  -4.123089\n" + 
				"13  -0.8505653\n" + 
				"14  -2.35355\n" + 
				"15  -3.491564\n" + 
				"16  4.514983\n" + 
				"17  4.985391\n" + 
				"18  0.9781292\n" + 
				"19  1.033741\n" + 
				"20  -4.525197\n" + 
				"2 0 20\n" + 
				"1  -4.399928\n" + 
				"2  -2.914221\n" + 
				"3  1.606918\n" + 
				"4  -3.253144\n" + 
				"5  1.953697\n" + 
				"6  2.610893\n" + 
				"7  -4.159857\n" + 
				"8  -2.684023\n" + 
				"9  -0.5837532\n" + 
				"10  4.664122\n" + 
				"11  2.967706\n" + 
				"12  -2.534048\n" + 
				"13  4.107646\n" + 
				"14  1.168511\n" + 
				"15  1.725941\n" + 
				"16  -2.042272\n" + 
				"17  -1.258555\n" + 
				"18  3.303622\n" + 
				"19  -4.526753\n" + 
				"20  -3.948493\n" + 
				"3 0 20\n" + 
				"1  2.178612\n" + 
				"2  -4.737\n" + 
				"3  2.000981\n" + 
				"4  -4.785099\n" + 
				"5  0.662206\n" + 
				"6  -3.698233\n" + 
				"7  -2.715192\n" + 
				"8  -2.439115\n" + 
				"9  1.501156\n" + 
				"10  -1.19647\n" + 
				"11  0.2429913\n" + 
				"12  -4.257285\n" + 
				"13  3.209707\n" + 
				"14  -3.44234\n" + 
				"15  -3.358183\n" + 
				"16  -0.7068589\n" + 
				"17  2.919763\n" + 
				"18  -3.810506\n" + 
				"19  4.928933\n" + 
				"20  -2.898943\n" + 
				"4 0 20\n" + 
				"1  0.9368755\n" + 
				"2  -0.9401269\n" + 
				"3  2.72155\n" + 
				"4  3.170702\n" + 
				"5  3.620014\n" + 
				"6  1.508352\n" + 
				"7  -1.208493\n" + 
				"8  0.1014817\n" + 
				"9  2.028655\n" + 
				"10  -0.8115116\n" + 
				"11  4.863702\n" + 
				"12  0.5827163\n" + 
				"13  2.302333\n" + 
				"14  -0.1057672\n" + 
				"15  -4.432924\n" + 
				"16  -5.05634\n" + 
				"17  -2.739784\n" + 
				"18  -2.311332\n" + 
				"19  3.641276\n" + 
				"20  -3.400951";
		return chromosomeList;
		
	}
	public String convertChromosomeListToIsland() {
		//TODO: do the actual convertion
		return "Noch nicht implementiert";
	}
	
    private String readFile(File file) 
    {
        String content = "";
        try
        {
            content = new String ( Files.readAllBytes( Paths.get(file.getAbsolutePath()) ) );
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return content;
    }
    private String[] getChromosomes(String chromosomeList) {
    	String[] chromosomes = chromosomeList.split("10000");
		return chromosomes;
    }
    private String getNumOfGens(String chromosome) {
    	String[] chromosomeHeaders = chromosome.split("\\s+");
    	String numOfGens = chromosomeHeaders[3];
    	return numOfGens;
    }

}
