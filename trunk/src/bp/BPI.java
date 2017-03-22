package bp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;



public class BPI {
		
		int number_of_layers=3;						//3
		int number_of_input_nodes=256;				//150
		int number_of_output_nodes=6;	     		//16
		int maximum_layers=256;					    //256
		int maximum_number_of_sets=100;				//100
		int number_of_input_sets;					        
		int epochs =300;	   							    //300 & 600(For Latin Arial)
		float error_threshold=0.00001F;				//0.0002F
		

		float learning_rate=150F;							//150F
		float slope=0.014F;									//0.014F	
		int weight_bias=30;									//30

		int[] layers=new int [number_of_layers];
		float[] current_input=new float [number_of_input_nodes];
		float[][] input_set=new float [number_of_input_nodes][maximum_number_of_sets];
		int[] desired_output=new int [number_of_output_nodes];
		int[][] desired_output_set=new int [maximum_number_of_sets][number_of_output_nodes];
		float[][] node_output=new float [number_of_layers][maximum_layers];
		float[][][] weight=new float [number_of_layers][maximum_layers][maximum_layers];
		float[][] error=new float [number_of_layers][maximum_layers];		
		int[] output_bit=new int [number_of_output_nodes];	
		int[] desired_output_bit = new int [number_of_output_nodes];

		int[][] ann_input_value=new int [32][32];
		
		
		int rec_width = 5;
		int rec_height = 5;
		int x_org=498;
		int y_org=120;
		int matrix_width=32;
		int matrix_height=32;
		int image_start_pixel_x=0;
		int image_start_pixel_y=0;
		int[] line_top=new int [20];
		int[] line_bottom=new int [20];
		int current_line=0;
		int number_of_lines=0;
		boolean line_present=true;
		boolean character_valid=true;
		boolean character_present=true;
		boolean trainer_thread_created=false;

		
					
		int input_image_height;
		int input_image_width;
		int top,bottom,left,right;	
		int prev_right=20;
		int character_height;
		int character_width;		
		
		static final String[][] data = new String[][] {		
			{"0","0000000000000000000000000000000000000000000000000011110000000000011001000000000001000010000000000100001000000000010000100000000001000010000000000100001000000000010000100000000001000110000000000010110000000000000110000000000000000000000000000000000000000000"},
			{"1","0000000000000000000000000000000000000000000000000000100000000000000110000000000001111000000000000101100000000000000110000000000000011000000000000001100000000000000110000000000000011000000000000001100000000000000110000000000000000000000000000000000000000000"},
			{"2","0000000000000000000000000000000000000000000000000011110000000000011001100000000001000010000000000000011000000000000001000000000000001100000000000000100000000000000100000000000000100000000000000111111000000000011111100000000000000000000000000000000000000000"},
			{"3","0000000000000000000000000000000000000000000000000011110000000000011001100000000001000010000000000000011000000000000011000000000000001100000000000000011000000000000000100000000001000010000000000110110000000000000110000000000000000000000000000000000000000000"},
			{"4","0000000000000000000000000000000000000000000000000000010000000000000011000000000000011100000000000001010000000000001001000000000001100100000000000100010000000000111111110000000000000100000000000000010000000000000001000000000000000000000000000000000000000000"},
			{"5","0000000000000000000000000000000000000000000000000011111000000000011000000000000001000000000000000101000000000000011111000000000001000110000000000000001000000000000000100000000011000110000000000110110000000000001110000000000000000000000000000000000000000000"},
			{"6","0000000000000000000000000000000000000000000000000000100000000000000110000000000000010000000000000011000000000000011111000000000001100010000000000100001100000000010000110000000001000010000000000110011000000000001111000000000000000000000000000000000000000000"},
			{"7","0000000000000000000000000000000000000000000000000111111000000000000000100000000000000110000000000000010000000000000011000000000000001000000000000001100000000000000100000000000000010000000000000011000000000000001100000000000000000000000000000000000000000000"},
			{"8","0000000000000000000000000000000000000000000000000011110000000000010001100000000001000010000000000100011000000000011111000000000001111100000000000100001000000000110000100000000001000010000000000110011000000000001111000000000000000000000000000000000000000000"},
			{"9","0000000000000000000000000000000000000000000000000011110000000000010001100000000011000010000000001100001000000000110001100000000001101100000000000011110000000000000010000000000000011000000000000001000000000000001100000000000000000000000000000000000000000000"},
		};
		
		
		public static void main(String[] argu) {
			
			BufferedImage image;
			StringBuffer sb = new StringBuffer();
			try {
				image = ImageIO.read(new File("images/number/5.bmp"));
				 int[] p = image.getRGB(0,0,image.getWidth(),image.getHeight(),new int[image.getWidth()*image.getHeight()],0,image.getWidth());
			     for(int i=0;i<image.getHeight();i++) {
					boolean f = false;
				  	for(int j=0;j<image.getWidth();j++) {
				   		if (p[i*image.getWidth()+j] == -1) {
				   			f = true;
				   			break;
				   		}
				   	}
				   	if (!f) {
				   		continue;
				   	}
				   	for(int j=0;j<image.getWidth();j++) {
				   		System.out.print(p[i*image.getWidth()+j]==-1?" ":"*"+(j==image.getWidth()-1?"\n":""));
				   		sb.append(p[i*image.getWidth()+j]==-1?"0":"1");
				   	}
				   	System.out.println();
				    }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//BPI bp = new BPI(sb.toString());
			BPI bp = new BPI("0000000000000000000000000000000000000000000000000011110000000000010001100000000011000010000000001100001000000000110001100000000001101100000000000011110000000000000010000000000000011000000000000001000000000000001100000000000000000000000000000000000000000000");
			bp.train();
			bp.run();
		}
		
		private void run() {
			
			for(int k=0;k<number_of_input_sets;k++)
			{
				for(int i=0;i<16;i++) {
					for(int j=0;j<16;j++)
					{
						char c = input.charAt(i * 16 + j);
						input_set[i*16+j][k] = (c == '1' ? 1 : 0);						
					}
				}
			}
			
			calculate_outputs();
			for(int i=0;i<number_of_output_nodes;i++) {				
				output_bit[i]=threshold(node_output[number_of_layers-1][i]);
			}
			System.out.println(Arrays.toString(output_bit));
		}

		public int threshold(float val)
		{
			if(val<0.5)					
				return 0;
			else
				return 1;
		}

		private String input;
		
		public BPI(String input) {
			this.input = input;
		}
		
		public void form_network()
		{
			layers[0]=number_of_input_nodes;
			layers[number_of_layers-1]=number_of_output_nodes;
			for(int i=1;i<number_of_layers-1;i++) {
				layers[i]=maximum_layers;
			}
		}
		public void initialize_weights()
		{
			for(int i=1;i<number_of_layers;i++) {
				for(int j=0;j<layers[i];j++) {
					for(int k=0;k<layers[i-1];k++) {
						float w = (float)(Math.random() * weight_bias * 2 - weight_bias);
						weight[i][j][k] = w;
					}
				}
			}
		}
		
		public void train() {
			
			number_of_input_sets = data.length;
			
			form_network();
			initialize_weights();			
			form_input_set();
			form_desired_output_set();
			right = 1;
			epochs = 100000;
			train_network();
		}
		
		public void map_ann_input_matrix()
		{			
			for( int j = 0; j < matrix_height; j++)			
				for(int i= 0; i < matrix_width; i++ )	
				{														
					char c = input.charAt(j * matrix_height + i);	
					if(c == '1') {
						ann_input_value[i][j]=1;
					} else {
						ann_input_value[i][j]=0;
					}
				}
		}
		
		public void form_input_set()
		{			
			for(int k=0;k<number_of_input_sets;k++)
			{
				for(int i=0;i<16;i++) {
					for(int j=0;j<16;j++)
					{
						String d = data[k][1];
						char c = d.charAt(i * 16 + j);
						input_set[i*16+j][k] = (c == '1' ? 1 : 0);						
					}
				}
			}
		}
		public void form_desired_output_set()
		{
			for(int i=0;i<number_of_input_sets;i++)	{
				for(int j=0;j<number_of_output_nodes;j++) {
					desired_output_set[i][j]=getOutputBit(i, j);
				}
			}
			
			for(int i=0;i<number_of_input_sets;i++)	{
				System.out.println(Arrays.toString(desired_output_set[i]));
			}
		}		
		
		private int getOutputBit(int i, int j) {
			String r = data[i][0];
			int in = Integer.parseInt(r) + '0';
			String s = Integer.toBinaryString(in);
			if (s.length() < 6) {
				s = "000000".substring(0, 6 - s.length()) + s;
			}
			char c = s.charAt(j);
			return c == '1' ? 1 : 0;
		}

		public void train_network()
		{									
			int set_number;	
			float average_error=0.0F;
			int epoch=0;
			for(epoch=0;epoch<=epochs;epoch++)
			{
				average_error=0.0F;
				for(int i=0;i<number_of_input_sets;i++)
				{					
					set_number= i;
					get_inputs(set_number);
					get_desired_outputs(set_number);
					calculate_outputs();			
					calculate_errors();
					calculate_weights();
					average_error=average_error+get_average_error();
				}								

				average_error=average_error/number_of_input_sets;
				if(average_error<error_threshold)
				{
					break;			
				}				
			}
			System.out.println("Finished at " + epoch + " average error = " + average_error);
		}
			
		public void get_inputs(int set_number)
		{
			for(int i=0;i<number_of_input_nodes;i++) {
				current_input[i]=input_set[i][set_number];
			}
		}
		public void get_desired_outputs(int set_number)
		{
			for(int i=0;i<number_of_output_nodes;i++) {
				desired_output[i]=desired_output_set[set_number][i];
			}
		}		
		public void calculate_outputs()
		{
			float f_net;
			int number_of_weights;
			for(int i=0;i<number_of_layers;i++)
				for(int j=0;j<layers[i];j++)
				{
					f_net=0.0F;
					if(i==0) {
						number_of_weights=1;
					} else {
						number_of_weights=layers[i-1];
					}
					
					for(int k=0;k<number_of_weights;k++) {
						if(i==0)
							f_net=current_input[j];
						else
							f_net=f_net+node_output[i-1][k]*weight[i][j][k];
					}
					node_output[i][j]=sigmoid(f_net);
				}			
		}
		public float sigmoid(float f_net)
		{						
			//float result=(float)(1/(1+Math.Exp (-1*slope*f_net)));		//Unipolar
			float result=(float)((2/(1+Math.exp(-1*slope*f_net)))-1);		//Bipolar			
			return result;
		}
		public float sigmoid_derivative(float result)
		{
			//float derivative=(float)(result*(1-result));					//Unipolar
			float derivative=(float)(0.5F*(1-Math.pow(result,2)));			//Bipolar			
			return derivative;
		}

		
		public void calculate_errors()
		{
			float sum=0.0F;
			for(int i=0;i<number_of_output_nodes;i++) {				
				error[number_of_layers-1][i]=(float)((desired_output[i]-node_output[number_of_layers-1][i])*sigmoid_derivative(node_output[number_of_layers-1][i]));
			}
			for(int i=number_of_layers-2;i>=0;i--)
				for(int j=0;j<layers[i];j++)
				{
					sum=0.0F;
					for(int k=0;k<layers[i+1];k++) {
						sum=sum+error[i+1][k]*weight[i+1][k][j];
					}
					error[i][j]=(float)(sigmoid_derivative(node_output[i][j])*sum);
				}
		}
		public float get_average_error()
		{			
			float average_error=0.0F;
			for(int i=0;i<number_of_output_nodes;i++) {		
				average_error = average_error + Math.abs(error[number_of_layers-1][i]);
			}
			average_error=average_error/number_of_output_nodes;
			return Math.abs(average_error);
		}
		public void calculate_weights()
		{
			for(int i=1;i<number_of_layers;i++)
				for(int j=0;j<layers[i];j++)
					for(int k=0;k<layers[i-1];k++)
					{
						weight[i][j][k]=(float)(weight[i][j][k]+learning_rate*error[i][j]*node_output[i][k]);						
					}
		}
		
}