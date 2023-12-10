package intro2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;


public class Frame extends JFrame implements ActionListener, KeyListener
{
	String passage=""; //Passage we get
	String typedPass=""; //Passage the user types
	String message=""; //Message to display at the end of the TypingTest
	 
	int typed=0; //typed stores till which character the user has typed
	int count=0;
	int WPM;
	int CPM;
	
	double start; 
	double end; 
	double elapsed;
	double seconds;
	
	boolean running; //If the person is typing
	boolean ended; //Whether the typing test has ended or not
	
	final int SCREEN_WIDTH;
	final int SCREEN_HEIGHT;
	final int DELAY=100; 
	
	JButton button; 
	Timer timer;
	JLabel label; 
	
	public Frame()
	{
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		SCREEN_WIDTH=840;
		SCREEN_HEIGHT=400;
		this.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
		this.setVisible(true); 
		this.setLocationRelativeTo(null); 
		
		button=new JButton("Start");
		button.setFont(new Font("MV Boli",Font.BOLD,30));
		button.setForeground(Color.RED);
		button.setVisible(true);
		button.addActionListener(this);
		button.setFocusable(false);
		button.setBackground(Color.BLUE);
		button.setAutoscrolls(true);
		
		
		label=new JLabel();
		label.setText("Click Start to Play Speed Typing");
		label.setFont(new Font("MV Boli",Font.BOLD,30));
		label.setVisible(true);
		label.setOpaque(true);
		label.setHorizontalAlignment(JLabel.CENTER); 
		label.setBackground(Color.GRAY);
		label.setForeground(Color.white);
		
		this.add(button, BorderLayout.SOUTH);
		this.add(label, BorderLayout.NORTH);
		this.getContentPane().setBackground(Color.YELLOW);
		this.addKeyListener(this);
		this.setFocusable(true); 
		this.setResizable(false);
		this.setTitle("SPEED TYPING CALCULATOR");
		this.revalidate(); 
	}

	@Override
	public void paint(Graphics g)	
	{
		super.paint(g);
		draw(g); 
	}
	public void draw(Graphics g)
	{
		g.setFont(new Font("MV Boli", Font.BOLD, 25));
		
		if(running)
		{
			//This will put our passage on the screen 
			if(passage.length()>1) 
			{
				g.drawString(passage.substring(0,50), g.getFont().getSize(), g.getFont().getSize()*5);
				g.drawString(passage.substring(50,100), g.getFont().getSize(), g.getFont().getSize()*7);
				g.drawString(passage.substring(100,150), g.getFont().getSize(), g.getFont().getSize()*9);
				g.drawString(passage.substring(150,200), g.getFont().getSize(), g.getFont().getSize()*11);
			}
			
			//Displaying correctly typed passage in GREEN
			
			g.setColor(Color.GREEN);			
			
			if(typedPass.length()>0)
			{ 
				//This is if the typedPassages length is greater than 0 and less than 50
				
				if(typed<50) //if the typed index is in the first line
					g.drawString(typedPass.substring(0,typed), g.getFont().getSize(),g.getFont().getSize()*5); //From the first letter to the currently typed one in green
				else
					g.drawString(typedPass.substring(0,50), g.getFont().getSize(),g.getFont().getSize()*5); //If the typed character exceeds 50 we can directly show the whole line in green
			}	
			
			if(typedPass.length()>50)
			{
				if(typed<100) 
					g.drawString(typedPass.substring(50,typed), g.getFont().getSize(),g.getFont().getSize()*7);
				else
					g.drawString(typedPass.substring(50,100), g.getFont().getSize(),g.getFont().getSize()*7);
			}
			
			if(typedPass.length()>100)
			{
				if(typed<150) 
					g.drawString(typedPass.substring(100,typed), g.getFont().getSize(),g.getFont().getSize()*9);
				else
					g.drawString(typedPass.substring(100,150), g.getFont().getSize(),g.getFont().getSize()*9);
			}
			
			if(typedPass.length()>150)
				g.drawString(typedPass.substring(150,typed), g.getFont().getSize(),g.getFont().getSize()*11);
			running=false;									 //Once we have made the line green we can make running false so that it does not keep repainting
															//Since when we type again running will become true and it will make the substring from the start of line to next character green
		}
		if(ended)
		{
			if(WPM<=40)
				message="You are a DECENT Typist";
			else if(WPM>40 && WPM<=60)
				message="You are a GOOD Typist";
			else if(WPM>60 && WPM<=100)
				message="You are an EXCELLENT Typist";
			else
				message="You are an ELITE Typist";
			
			FontMetrics metrics=getFontMetrics(g.getFont());
			g.setColor(Color.BLUE);
			g.drawString("Typing Test Completed!", (SCREEN_WIDTH-metrics.stringWidth("Typing Test Completed!"))/2, g.getFont().getSize()*6);
			
			g.setColor(Color.BLACK);
			g.drawString("Typing Speed: "+WPM+" Words Per Minute "+" i.e "+CPM+" charecters per min", (SCREEN_WIDTH-metrics.stringWidth("Typing Speed: "+WPM+" Words Per Minute "+" i.e "+CPM+" charecters per min"))/2, g.getFont().getSize()*9);
			g.drawString(message, (SCREEN_WIDTH-metrics.stringWidth(message))/2, g.getFont().getSize()*11);
			
			timer.stop();
			ended=false; 
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) 			//keyTyped uses the key Character which can identify capital and lowercase difference in keyPressed it takes unicode so it also considers shift which creates a problem
	{
		if(passage.length()>1)
		{
			if(count==0)
				start=LocalTime.now().toNanoOfDay();
			else if(count==200) 											//Once all 200 characters are typed we will end the time and calculate time elapsed
			{
				end=LocalTime.now().toNanoOfDay();
				elapsed=end-start;
				seconds=elapsed/1000000000.0; 								//nano/1000000000.0 is seconds
				WPM=(int)(((200.0/5)/seconds)*60); 						//number of character by 5 is one word by seconds is words per second * 60 WPM
				CPM =(int)((200.0/seconds)*60);							//no of charecter by second is charecter per second *60 is CPM
				ended=true;
				running=false;
				count++;
			}
			char[] pass=passage.toCharArray();
			if(typed<200)
			{
				running=true;
				if(e.getKeyChar()==pass[typed]) 
				{
					typedPass=typedPass+pass[typed]; //To the typed Passage we are adding what is currently typed
					typed++;
					count++; 
				} //If the typed character is not equal to the current position it will not add it to the typedPassage, so the user needs to type the right thing
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		
	}

	@Override 
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource()==button)
		{
			passage=getPassage();
			timer=new Timer(DELAY,this);
			timer.start();
			running=true;
			ended=false;
			
			typedPass="";
			message="";
			
			typed=0;
			count=0;
		}
		if(running)
			repaint();
		if(ended)
			repaint();
	}
	public static String getPassage()
	{
		ArrayList<String> Passages=new ArrayList<String>();
		String pas1="An old man lived in the village. He was one of the most unfortunate people in the world. The whole village was tired of him; he was always gloomy, he constantly complained and was always in a bad mood.The longer he lived, the more bile he was becoming and the more poisonous";
		String pas2="A virtual assistant (typically abbreviated to VA) is generally self-employed and provides professional administrative, technical, or creative assistance to clients remotely from a home office. Many touch typists also use keyboard shortcuts or hotkeys when typing on a computer";
		String pas3="Nasir, a small boy, found a crystal ball behind the banyan tree of his garden. The tree told him that it would grant him a wish. He was very happy and he thought hard, but unfortunately, he could not come up with anything he wanted. So, he kept the crystal ball in his bag and ";
		String pas4="Income before securities transactions was up 10.8 percent from $13.49 million in 1982 to $14.95 million in 1983. Earnings per share (adjusted for a 10.5 percent stock dividend distributed on August 26) advanced 10 percent to $2.39 in 1983 from $2.17 in 1982. Earnings may rise ";
		String pas5="Historically, the fundamental role of pharmacists as a healthcare practitioner was to check and distribute drugs to doctors for medication that had been prescribed to patients. In more modern times, pharmacists advise patients and health care providers on the selection, dosage";
		String pas6="Proofreader applicants are tested primarily on their spelling, speed, and skill in finding errors in the sample text. Toward that end, they may be given a list of ten or twenty classically difficult words and a proofreading test, both tightly timed. The proofreading test will o";
		String pas7="In one study of average computer users, the average rate for transcription was 33 words per minute, and 19 words per minute for composition. In the same study, when the group was divided into \"fast\", \"moderate\" and \"slow\" groups, the average speeds were 40 wpm, 35 wpm, an";
		String pas8="A teacher's professional duties may extend beyond formal teaching. Outside of the classroom teachers may accompany students on field trips, supervise study halls, help with the organization of school functions, and serve as supervisors for extracurricular activities. In some e";
		String pas9="Web designers are expected to have an awareness of usability and if their role involves creating mark up then they are also expected to be up to date with web accessibility guidelines. The different areas of web design include web graphic design; interface design; authoring, i";
		String pas10="Once there lived a greedy man in a small town. He was very rich, and he loved gold and all things fancy. But he loved his daughter more than anything. One day, he chanced upon a fairy. The fairy’s hair was caught in a few tree branches. He helped her out, but as his greediness ";
		
		Passages.add(pas1);
		Passages.add(pas2);
		Passages.add(pas3);
		Passages.add(pas4);
		Passages.add(pas5);
		Passages.add(pas6);
		Passages.add(pas7);
		Passages.add(pas8);
		Passages.add(pas9); 
		Passages.add(pas10);
		
		Random rand=new Random();
		int place=(rand.nextInt(10));
		
		String toReturn=Passages.get(place).substring(0,200); 
		if(toReturn.charAt(199)==32) 
		{
			toReturn=toReturn.strip(); 
			toReturn=toReturn+"."; //Adding a full stop at the last instead of a space
		}
		return(toReturn); 
	}
}