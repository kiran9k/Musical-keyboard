package piano_keyboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.MaskFormatter;



/**
 * @author Kiran K 
 */
public class piano_GUI {
	int height,width;
	int menu_height=50;
	int key_width=20;
	private final MidiChannel channel;
	boolean still_playing=false;
	private static int OBOE = 01;//00-99 tune selector
	private int start_tone=60; // gives the starting value for sound
	List<String> keys_names=Arrays.asList("A","W","S","E","D","F","T","G","Y","H","U","J","K","O","L","P",";","\'","}");
	List<Integer> tone_values=new ArrayList<Integer>();
	List<Integer> key_event_values=new ArrayList<Integer>();
	
	
	public piano_GUI() throws MidiUnavailableException
	{	
		this.channel = initMidiChannel(OBOE);
		intialize_tone_values();
		System.out.println("running");
		JFrame main_frame=new JFrame("Keyboard UI");
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main_frame.setBackground(Color.white);		
		height=400;
		width=840;
		main_frame.setLayout(new GridBagLayout());
		main_frame.setLocation(100,700) ;
	    GridBagConstraints c = new GridBagConstraints();
	    c.gridx=0;
	    c.gridy=0;
	    //c.ipady=1;
	    c.weightx=1;
	   // c.fill = GridBagConstraints.VERTICAL;
		main_frame.setSize(width,height);
		main_frame.setResizable(false);
		
		JPanel sub_frame1=new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		JPanel sub_frame2=new JPanel();
		
		sub_frame1.setName("options");
		sub_frame2.setName("keyboard");
		//sub_frame1.setBackground(Color.white);
		sub_frame1.setPreferredSize(new Dimension(width,menu_height));
		//sub_frame1.setLayout(new GridBagLayout());
		GridBagConstraints csf=new GridBagConstraints();
		csf.gridx=0;
		csf.gridy=0;
		csf.weightx=1;
		sub_frame1.add(tune_select(width,menu_height));
		sub_frame1.setBorder(BorderFactory.createLineBorder(Color.black));
		sub_frame2.setPreferredSize(new Dimension(width,height/2));
		sub_frame2=create_piano(sub_frame2,width,height/2);
		
		main_frame.add(sub_frame1,c);
		c.gridy=1;
		sub_frame2.setFocusable(true);
		sub_frame2.requestFocusInWindow();		
		main_frame.add(sub_frame2,c);		
		main_frame.setVisible(true);
        //main_frame.requestFocusInWindow();
     //   main_frame.setFocusableWindowState(true);
		main_frame.pack();
		
	}
	
	public void intialize_tone_values()
	{
		
		for (int i=0;i<keys_names.size();i++)
		{
			tone_values.add(i+start_tone+12);
		}
		
		key_event_values.add(KeyEvent.VK_A);
		key_event_values.add(KeyEvent.VK_W);
		key_event_values.add(KeyEvent.VK_S);
		key_event_values.add(KeyEvent.VK_E);
		key_event_values.add(KeyEvent.VK_D);
		key_event_values.add(KeyEvent.VK_F);
		key_event_values.add(KeyEvent.VK_T);
		key_event_values.add(KeyEvent.VK_G);
		key_event_values.add(KeyEvent.VK_Y);
		key_event_values.add(KeyEvent.VK_H);
		key_event_values.add(KeyEvent.VK_U);
		key_event_values.add(KeyEvent.VK_J);
		key_event_values.add(KeyEvent.VK_K);
		key_event_values.add(KeyEvent.VK_O);
		key_event_values.add(KeyEvent.VK_L);
		key_event_values.add(KeyEvent.VK_P);
		key_event_values.add(KeyEvent.VK_SEMICOLON);
		key_event_values.add(KeyEvent.VK_QUOTE);
		key_event_values.add(KeyEvent.VK_CLOSE_BRACKET);
		
	}
	
	public JPanel three_keys(int w,int h,int value)
	{
		JPanel keys=new JPanel();
		//GridBagConstraints c1 = new GridBagConstraints();
		keys.setPreferredSize(new Dimension(6*key_width,h));
		keys.setLayout(new GridLayout(2,1));
		JPanel top_3keys=new JPanel();
		JPanel bottom_3keys=new JPanel();
		top_3keys.setPreferredSize(new Dimension(6*key_width,h/2));
		top_3keys.setBackground(Color.white);
		bottom_3keys.setPreferredSize(new Dimension(6*key_width,h/2));
		keys.add(top_3keys);
		keys.add(bottom_3keys);
		top_3keys.setLayout(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridy=0;
		//c1.gridwidth=key_width;
		c1.gridx=0;
		//black buttons
		final JButton[] top_3buttons=new JButton[2];
		
		for(int i=0;i<2;i++)
		{
			top_3buttons[i]=new JButton();
			top_3buttons[i].setBorder(null);
			top_3buttons[i].setForeground(Color.white);
			top_3buttons[i].setBackground(Color.black);
			top_3buttons[i].setPreferredSize(new Dimension(key_width/2,h/2));
			final int tone ;
			final int key_press;
			if(i==0)
			{
				
				c1.ipadx=(int)1.5*key_width;
				tone=start_tone+(12*value)+1;
				
				
			}
			else
			{
				//c1.ipadx=20;
				c1.insets=new Insets(0,key_width,0,0);
				//c1.insets( new Insets(0,30,0,0));
				c1.gridx=1;
				tone=start_tone+(12*value)+3;
			}
			if(value>0)
			{
				key_press=1+(2*i)+(12*(value-1));
				top_3buttons[i].setText(keys_names.get(key_press));
				top_3buttons[i].getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key_event_values.get(key_press),0), keys_names.get(key_press));
				top_3buttons[i].getActionMap().put(keys_names.get(key_press),buttonPressed(top_3buttons[i],true));
					
			}
			
			
			top_3buttons[i].addActionListener(new ActionListener(){				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					myplay(tone);
					
				}
				
			}
			);
			top_3keys.add(top_3buttons[i],c1);
		}
		bottom_3keys.setLayout(new GridLayout(1,3));
		JButton[] bottom_3buttons=new JButton[3];
		for(int i=0;i<3;i++)
		{
			final int tone ;
			bottom_3buttons[i]=new JButton();
			bottom_3buttons[i].setBackground(Color.white);
			bottom_3buttons[i].setPreferredSize(new Dimension(key_width,h/2));
			if(i!=2)
				bottom_3buttons[i].setBorder(BorderFactory.createMatteBorder(0, 0,0,1,Color.black));
			else
				bottom_3buttons[i].setBorder(BorderFactory.createMatteBorder(0, 0,0,0,Color.black));
			tone=start_tone+(12*value)+(i*2);
			if(value>0)
			{
				int key_press=(2*i)+(12*(value-1));
				bottom_3buttons[i].setText(keys_names.get(key_press));
				bottom_3buttons[i].getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key_event_values.get(key_press),0), keys_names.get(key_press));
				bottom_3buttons[i].getActionMap().put(keys_names.get(key_press),buttonPressed(bottom_3buttons[i],false));
			}
			bottom_3buttons[i].addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					myplay(tone);
					
				}
				
			});
			bottom_3keys.add(bottom_3buttons[i]);
			
		}
		return keys;		
	}
	
	private Action buttonPressed(final JButton jButton,final Boolean black) {
		// TODO Auto-generated method stub		
		
		AbstractAction buttonPressed = new AbstractAction() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	int index=keys_names.indexOf(e.getActionCommand().toUpperCase());
	            if(index>=0)
	            {
	    
	            	jButton.setEnabled(false);
	            	myplay(tone_values.get(index));
	            	//jButton.setEnabled(true);
	            	try {
						Thread.sleep(1);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            	//jButton.setEnabled(true);
	            	//.setSelected(true);
	        		//jButton.setBackground(Color.white);
	        	    jButton.setEnabled(true);

	            }
	           
	        }
	    };
		return buttonPressed;
	}

	
	public JPanel four_keys(int w,int h,int value)
	{
		JPanel keys=new JPanel();
		//GridBagConstraints c1 = new GridBagConstraints();
		keys.setPreferredSize(new Dimension(8*key_width,h));
		keys.setLayout(new GridLayout(2,1));
		JPanel top_4keys=new JPanel();
		JPanel bottom_4keys=new JPanel();
		top_4keys.setPreferredSize(new Dimension(6*key_width,h/2));
		top_4keys.setBackground(Color.white);
		bottom_4keys.setPreferredSize(new Dimension(6*key_width,h/2));
		keys.add(top_4keys);
		keys.add(bottom_4keys);
		top_4keys.setLayout(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridy=0;
		//c1.gridwidth=key_width;
		c1.gridx=0;
		//black buttons
		JButton[] top_4buttons=new JButton[3];
		for(int i=0;i<3;i++)
		{
			final int tone;
			top_4buttons[i]=new JButton();
			top_4buttons[i].setBorder(null);
			top_4buttons[i].setBackground(Color.black);
			top_4buttons[i].setPreferredSize(new Dimension(key_width/2,h/2));
			if(i==0)
			{
				c1.ipadx=(int)1.5*key_width;
				tone=start_tone+(12*value)+6;
				
			}
			else
			{
				//c1.ipadx=20;				
				c1.insets=new Insets(0,key_width,0,0);
				//c1.insets( new Insets(0,30,0,0));
				c1.gridx=1;
				if(i==2)
				{
					c1.gridx=2;
					tone=start_tone+(12*value)+10;
				}
				else
					tone=start_tone+(12*value)+8;
			}
			int key_press=6+(2*i)+(12*(value-1));
			if(value>0 && key_press<keys_names.size())
			{
				top_4buttons[i].setForeground(Color.white);
				top_4buttons[i].setText(keys_names.get(key_press));
				top_4buttons[i].getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key_event_values.get(key_press),0), keys_names.get(key_press));
				top_4buttons[i].getActionMap().put(keys_names.get(key_press),buttonPressed(top_4buttons[i],true));
			}
			
			
			top_4buttons[i].addActionListener(new ActionListener(){
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
					myplay(tone);
					
				}
				
			});
			top_4keys.add(top_4buttons[i],c1);
		}
		bottom_4keys.setLayout(new GridLayout(1,3));
		JButton[] bottom_4buttons=new JButton[4];
		for(int i=0;i<4;i++)
		{
			final int tone;
			bottom_4buttons[i]=new JButton();
			bottom_4buttons[i].setBackground(Color.white);
			bottom_4buttons[i].setPreferredSize(new Dimension(key_width,h/2));
			if(i!=3)
				bottom_4buttons[i].setBorder(BorderFactory.createMatteBorder(0, 0,0,1,Color.black));
			else
				bottom_4buttons[i].setBorder(BorderFactory.createMatteBorder(0, 0,0,0,Color.black));
			tone=start_tone+(12*value)+5+(2*i);
			int key_press=5+(2*i)+(12*(value-1));
			if(value>0 && key_press<keys_names.size())
			{
				
				bottom_4buttons[i].setText(keys_names.get(key_press));
				bottom_4buttons[i].getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key_event_values.get(key_press),0), keys_names.get(key_press));
				bottom_4buttons[i].getActionMap().put(keys_names.get(key_press),buttonPressed(bottom_4buttons[i],false));
				
			}
			bottom_4buttons[i].addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					myplay(tone);
				}
				
			});
			bottom_4keys.add(bottom_4buttons[i]);
			
		}
		return keys;		
		
	}
	
	public JPanel tune_select(int w,int h)
	{
		JPanel tune_selector=new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		tune_selector.setPreferredSize(new Dimension((int)w/3,h));
		JTextField name=new JTextField();
	
		name.setText("Tune(0-99): ");
		name.setEditable(false);
		name.setFont(new Font("Arial",Font.PLAIN,20));
		name.setPreferredSize(new Dimension(150,h));
		//name.setBackground(Color.black);
		//name.setPreferredSize(new Dimension((int)w/4,h));
		tune_selector.setLayout(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		JFormattedTextField value=new JFormattedTextField(NumberFormat.getNumberInstance());
		c1.gridy=0;
		c1.gridx=0;
		value.setText(String.valueOf(OBOE));
		value.setPreferredSize(new Dimension(40,h));
		value.addPropertyChangeListener(new PropertyChangeListener(){

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				// TODO Auto-generated method stub
				int x;
				try{
					x=Integer.parseInt(arg0.getNewValue().toString());
					if(x>0)
					{
						OBOE=Integer.valueOf(arg0.getNewValue().toString());
				        channel.programChange(OBOE);

					}
				}
				catch(NumberFormatException e)
				{
				//	System.out.println(e);
				}
			
			}
			
		});
		
	    
		tune_selector.add(name,c1);
		c1.gridx=1;
		tune_selector.add(value,c1);
		return tune_selector;
		
	}
	
	public  JPanel create_piano(JPanel key_label,int w,int h)
	{
		key_label.setLayout(new GridBagLayout());
		int value=0;
		GridBagConstraints c1 = new GridBagConstraints();
		JPanel key_set1a=three_keys(w,h,value);		
		JPanel key_set1b=four_keys(w,h,value);
		key_set1a.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.black));
		key_set1b.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.black));
		value++;
		JPanel key_set2a=three_keys(w,h,value);
		JPanel key_set2b=four_keys(w,h,value);
		key_set2a.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.black));
		key_set2b.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.black));
		
		value++;
		JPanel key_set3a=three_keys(w,h,value);
		JPanel key_set3b=four_keys(w,h,value);
		key_set3a.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.black));
		key_set3b.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.black));
		
		key_set1a.setBackground(Color.white);		
		c1.gridx=0;
		c1.gridy=0;
		c1.ipadx=0;
		c1.ipady=0;
		key_label.add(key_set1a,c1);
		key_set1b.setBackground(Color.white);
		c1.gridx=1;
		key_label.add(key_set1b,c1);
		c1.gridx=2;
		key_set2a.setBackground(Color.white);
		key_label.add(key_set2a,c1);
		c1.gridx=3;
		key_set2b.setBackground(Color.white);
		key_label.add(key_set2b,c1);
		c1.gridx=4;
		key_set3a.setBackground(Color.white);
		key_label.add(key_set3a,c1);
		c1.gridx=5;
		key_set3b.setBackground(Color.white);
		key_label.add(key_set3b,c1);
		return key_label;		
	}
	
	private static MidiChannel initMidiChannel(int instrument) throws MidiUnavailableException {
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        MidiChannel channel = synth.getChannels()[0];
        // MIDI instruments are traditionally numbered from 1,
        // but the javax.midi API numbers them from 0
        channel.programChange(instrument );
        channel.setChannelPressure(5);  // optional vibrato
        return channel;
    }

  
   
    public void myplay(int x)
    {
    	if(still_playing)
    		silence();
    	this.channel.noteOn(x, 255);
    	still_playing=true;
    	 
    }

    public void silence() {
        this.channel.allNotesOff();
    }
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	               
	                    try {
							new piano_GUI();
						} catch (MidiUnavailableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                
	            }
	        });
	    }
	

}
