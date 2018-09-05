import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Vector;
import java.util.Timer;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
class main_window {
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	static String VLCLIBPATH ="C:\\Program Files\\VideoLAN\\VLC";
	//private File file;
	signinwindow s1;
	String signinname;
	int si_id=-1;
	static int winactcnt;
	static {
		winactcnt=0;
	}
	ArrayList<account> accounts= new ArrayList<account>();
	ArrayList<channel> channels=new ArrayList<channel>();
	ArrayList<video> videos=new ArrayList<video>();
	Vector<search> searches=new Vector<search>();
	
	main_window()
	{
		JFrame main= new JFrame("Youtube");
		main.setSize(600, 400);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setLayout(new FlowLayout());
		JLabel lab= new JLabel();
		JButton but1 = new JButton("Show all logins");
		JButton but2 = new JButton("Play video from file");
		JButton signup_btn= new JButton("Sign up");
		JButton signin_btn= new JButton("Sign in");
		JButton signout_btn= new JButton("Sign out");
		JButton createchannel=new JButton("Create channel");
		JButton uploadvideo=new JButton("Upload video");
		JButton searchvid=new JButton("Search");
		//but1.setLocation(0,0);
		//but1.setPreferredSize(new Dimension(100,20));
		//but1.setBounds(0,100,20,100);
		but1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(int i=0;i<channel.channel_count;i++) {
					channel ob2=channels.get(i);
					System.out.println(ob2.channel_id+"\t"+ob2.account_id+"\t"+ob2.channel_name+"\t"+ob2.description);
				}
				
			}
			
		});
		signup_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				signupwindow();
				
			}
			
		});
		searchvid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchvidwindow();
				
			}
			
		});
		createchannel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(si_id>=0)
				{
				if(!(accounts.get(si_id).havechannel))
				{
					createchannelwindow(si_id);
				}
				else
				{
					JOptionPane.showMessageDialog(null,"Already have a channel linked with this account");
				}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "you have not signed in to your account");
				}	
			}
			
		});
		uploadvideo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(si_id>=0)
				{
				if(!(accounts.get(si_id).havechannel))
				{
					JOptionPane.showMessageDialog(null,"This account does not have a channel yet,to upload videos please create a channel first.");
				}
				else
				{
					uploadvideowindow(si_id);
				}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "you have not signed in to your account");
				}	
			}
			
		});
		signin_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				s1=new signinwindow();
				winactcnt++;
				
			}
			
		});
		signout_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(si_id==-1)
				{
					JOptionPane.showMessageDialog(null, "Not logged in");
				}
				else
				{
					si_id=-1;
					s1.isloggedin=null;
					lab.setText("");
				}
			}
			
		});
		but2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File file=openFile();
				if(file!=null)
				{
				String f=file.toString();
				videoplayframe(f);
				}
			}
			
		});
		main.add(lab);
		main.add(but1);
		main.setVisible(true);
		main.add(but2);
		main.add(signup_btn);
		main.add(signin_btn);
		main.add(signout_btn);
		main.add(createchannel);
		main.add(uploadvideo);
		main.add(searchvid);
		main.addWindowListener(new WindowAdapter() {
		      public void windowOpened(WindowEvent e) {
		        System.out.println("Window Opened Event");
		      }

		      public void windowClosing(WindowEvent e) {
		        System.out.println("Window Closing Event");
		      }

		      public void windowClosed(WindowEvent e) {
		        System.out.println("Window Close Event");
		      }

		      public void windowIconified(WindowEvent e) {
		        System.out.println("Window Iconified Event");
		      }

		      public void windowDeiconified(WindowEvent e) {
		        System.out.println("Window Deiconified Event");
		      }

		      public void windowActivated(WindowEvent e) {
		        System.out.println("Window Activated Event");
		        if(winactcnt>=1)
		        {
		        if(s1.isloggedin.equals("done")) {
		        	si_id=findaccount(s1.getusername());
		        	lab.setText("Logged in as "+s1.getusername());
		        	winactcnt--;
		        }
		        }
		      }

		      public void windowDeactivated(WindowEvent e) {
		        System.out.println("Window Deactivated Event");
		      }

		      public void windowStateChanged(WindowEvent e) {
		        System.out.println("Window State Changed Event");
		      }

		    });
		
	}

	public static void main(String args[])
	{
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), VLCLIBPATH);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				new main_window();
			}
		});
	}
/*	class customfocuslistener implements FocusListener{
		public void focusGained(FocusEvent e)
		{
			System.out.println("Main window in focus");
		}
		public void focusLost(FocusEvent e)
		{
			System.out.println("Main window focus lost");
		}
		}*/
	public void addaccount(account ob1) {
		accounts.add(ob1);
	}
	public int findaccount(String gmid) {
		for(int i=0;i<account.account_count;i++) {
			account ob2=accounts.get(i);
			if(ob2.gmailid.equals(gmid))
				return i;
		}
		return -1;
	}
	public int findchannel(int ac_id) {
		for(int i=0;i<channel.channel_count;i++) {
			channel ob2=channels.get(i);
			if(ob2.account_id==ac_id)
				return ob2.channel_id;
		}
		return -1;
	}
	 private File openFile()
     {
         JFileChooser fileChooser = new JFileChooser();

         fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
         int result = fileChooser.showOpenDialog( null );

         // user clicked Cancel button on dialog
         if ( result == JFileChooser.CANCEL_OPTION )
             return null;
         else
             return fileChooser.getSelectedFile();
     }
	public void searchvidwindow() {
		search.search_count=0;
		searches.clear();
		JFrame sframe=new JFrame("Search");
		sframe.setVisible(true);
		sframe.setSize(600, 400);
		JLabel l1=new JLabel("Enter search query");
		JTextField t1=new JTextField();
		t1.setColumns(20);
		JButton submit=new JButton("Submit");
		JButton play=new JButton("Play/Info");
		sframe.setLayout(new FlowLayout());
		sframe.add(l1);
		sframe.add(t1);
		sframe.add(submit);
		JList mylist=new JList();
		Vector<String>searchstrs=new Vector<String>();
		//Vector<JPanel> panels=new Vector<JPanel>();
		 submit.addActionListener(new ActionListener() {
				@SuppressWarnings("unchecked")
				public void actionPerformed(ActionEvent arg0) {
					String query=t1.getText();
					for(int i=0;i<video.video_count;i++) {
						if(videos.get(i).vid_name.equals(query)) {
							search o1=new search(videos.get(i).video_id,"video");
							searches.add(o1);
						}
					}
					for(int i=0;i<channel.channel_count;i++) {
						if(channels.get(i).channel_name.equals(query)) {
							search o1=new search(channels.get(i).channel_id,"channel");
							searches.add(o1);
						}
					}
					for(int i=0;i<search.search_count;i++)
					{
						if(searches.get(i).type.equals("video")) {
							String vid_name=videos.get(searches.get(i).id).vid_name;
							searchstrs.add("Video:"+vid_name);
						}
						 if(searches.get(i).type.equals("channel")) {
							 String cname=channels.get(searches.get(i).id).channel_name;
							 searchstrs.add("Channel:"+cname);
						 }
					}
				/*	 for(int i=0;i<search.search_count;i++) {
						 JPanel p1=new JPanel();
						 if(searches.get(i).type.equals("video")) {
							 String vname=videos.get(searches.get(i).id).vid_name;
							 JLabel l=new JLabel(vname);
							 JButton b=new JButton("play");
							 p1.setLayout(new FlowLayout());
							 p1.add(l);
							 p1.add(b);
							// p1.putClientProperty(arg0, arg1);
							 panels.add(p1);
						 }
						 if(searches.get(i).type.equals("channel")) {
							 String cname=channels.get(searches.get(i).id).channel_name;
							 JLabel l=new JLabel(cname);
							 JButton b=new JButton("info");
							 p1.setLayout(new FlowLayout());
							 p1.add(l);
							 p1.add(b);
							// p1.putClientProperty(arg0, arg1);
							 panels.add(p1);
						 }
					 }
					 JPanel[] panelarray=new JPanel[search.search_count+1];
					 for(int i=0;i<search.search_count;i++) {
				
						 panelarray[i]=panels.get(i);
					 }*/
					/* mylist.setCellRenderer(new DefaultListCellRenderer() {
				            @Override
				            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				                if (renderer instanceof JLabel && value instanceof CD) {
				                    // Here value will be of the Type 'CD'
				                    ((JLabel) renderer).setText(((CD) value).getName());
				                }
				                return renderer;
				            }
				        });*/
					 
					 mylist.setListData(searchstrs);
				}
				
			});
		 //JList list1=new JList(searches);
		 mylist.addListSelectionListener(new ListSelectionListener() {

	            @Override
	            public void valueChanged(ListSelectionEvent arg0) {
	                if (!arg0.getValueIsAdjusting()) {
	                  System.out.println(mylist.getSelectedIndex());
	                }
	            }
	        });
		 
		 play.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int sid=mylist.getSelectedIndex();
					if(searches.get(sid).type.equals("video")) {
						videoplayframe(videos.get(searches.get(sid).id).vid_path);
					}
					 if(searches.get(sid).type.equals("channel")) {
						 
					 }
				}
				
			});
		 sframe.add(new JScrollPane(mylist));
		 sframe.add(play);
		 //JList<ArrayList> mylist=new JList<ArrayList>(panels);
		//Canvas c=new Canvas();
		//c.setSize(600, 300);
		
	}
	public void uploadvideowindow(int accid) {
		JFrame upframe=new JFrame("Upload video");
		JLabel l1,l2,l3;
		JButton submit=new JButton("Submit");
		JButton select=new JButton("Select");
		l1=new JLabel("Video title");
		l2=new JLabel("Description");
		l3=new JLabel("Video path");
		l1.setBounds(20, 50, 150, 30);
		l2.setBounds(20, 100, 150, 30);
		l3.setBounds(20, 260, 150, 30);
		JTextField t1,t2,t3;  
	    t1=new JTextField();  
	    t1.setBounds(150,50, 200,30);  
	    t2=new JTextField();  
	    t2.setBounds(150,100, 200,90);  
	    t3=new JTextField();  
	    t3.setBounds(150,260, 200,30);
	    select.setBounds(400, 260, 100, 30);
	    submit.setBounds(100,300, 100,30);  
	    upframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String vidname=t1.getText();
				String viddesc=t2.getText();
				String vidpath=t3.getText();
				int channelid=findchannel(accid);
				if(channelid!=-1) {
				video ob1=new video(vidname,viddesc,vidpath,channelid);
				videos.add(ob1);
				upframe.dispose();;
				}
			}
			
		});
	    select.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File file=openFile();
				if(file!=null) {
					String path=file.getPath();
					t3.setText(path);
				}
			}
			
		});
	    upframe.add(t1);
	    upframe.add(t2);
	    upframe.add(t3);
	    upframe.add(l1); 
	    upframe.add(l2);
	    upframe.add(l3);
	    upframe.add(submit); 
	    upframe.add(select);
	    upframe.setSize(600,400);  
	    upframe.setLayout(null);  
	    upframe.setVisible(true);
	}
	public void createchannelwindow(int accid) {
		JFrame chframe=new JFrame("Create channel");
		JLabel l1,l2;
		JButton submit=new JButton("Submit");
		l1=new JLabel("Channel name");
		l2=new JLabel("Description");
		l1.setBounds(20, 100, 150, 30);
		l2.setBounds(20, 150, 150, 30);
		JTextField t1,t2;  
	    t1=new JTextField();  
	    t1.setBounds(150,100, 200,30);  
	    t2=new JTextField();  
	    t2.setBounds(150,150, 200,90);  
	    submit.setBounds(100,310, 100,30);  
	    chframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String chname=t1.getText();
				String chdesc=t2.getText();
				channel ob1=new channel(chname,chdesc,accid);
				channels.add(ob1);
				account o1=accounts.get(accid);
				o1.havechannel=true;
				chframe.dispose();;
			}
			
		});
	    chframe.add(t1);
	    chframe.add(t2);  
	    chframe.add(l1); 
	    chframe.add(l2); 
	    chframe.add(submit); 
	    chframe.setSize(600,400);  
	    chframe.setLayout(null);  
	    chframe.setVisible(true);
	}
	public void signupwindow() {
		JFrame signupframe=new JFrame("Sign up");
		JLabel l1,l2,l3;
		JButton submit=new JButton("Submit");
		l1=new JLabel("Gmail id:");
		l2=new JLabel("Password:");
		l3=new JLabel("Confirm Password:");
		l1.setBounds(20, 100, 150, 30);
		l2.setBounds(20, 150, 150, 30);
		l3.setBounds(20, 200, 150, 30);
		JTextField t1,t2,t3;  
	    t1=new JTextField();  
	    t1.setBounds(150,100, 200,30);  
	    t2=new JPasswordField();  
	    t2.setBounds(150,150, 200,30);  
	    t3=new JPasswordField();  
	    t3.setBounds(150,200, 200,30);  
	    t1.requestFocus();
	    submit.setBounds(100,250, 100,30);  
	    signupframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String gmid=t1.getText();
				String pwd=t2.getText();
				String confpwd=t3.getText();
				if(!(pwd.equals(confpwd))) {
					JOptionPane.showMessageDialog(null, "Passwords do not match");
					t2.setText(null);
					t3.setText(null);
				}
				else {
					account ob1=new account(gmid,pwd);
					addaccount(ob1);
					signupframe.dispose();
				}
			}
			
		});
	    signupframe.add(t1);
	    signupframe.add(t2);  
	    signupframe.add(t3);  
	    signupframe.add(l1); 
	    signupframe.add(l2); 
	    signupframe.add(l3); 
	    signupframe.add(submit); 
	    signupframe.setSize(600,400);  
	    signupframe.setLayout(null);  
	    signupframe.setVisible(true);  
	}
	class signinwindow{
		String uname=null;
		String isloggedin="false";
		JFrame signinframe;
	public signinwindow() {
		signinframe=new JFrame("Sign in");
		JLabel l1,l2;
		JButton submit=new JButton("Submit");
		l1=new JLabel("Gmail id:");
		l2=new JLabel("Password:");
		l1.setBounds(20, 100, 150, 30);
		l2.setBounds(20, 150, 150, 30);
		JTextField t1,t2;  
	    t1=new JTextField();  
	    t1.setBounds(150,100, 200,30);  
	    t2=new JPasswordField();  
	    t2.setBounds(150,150, 200,30);   
	    submit.setBounds(100,250, 100,30);  
	    signinframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String gmid=t1.getText();
				String pwd=t2.getText();
				int ac_id=findaccount(gmid);
				if(ac_id==-1) {
					JOptionPane.showMessageDialog(null, "Account not found");
				}
				else {
					account ob= accounts.get(ac_id);
					if(ob.validate_crid(gmid,pwd)) {
						uname=gmid;
						isloggedin="done";
						signinframe.dispose();
					}
					else
						JOptionPane.showMessageDialog(null, "Incorrect password");
				}
			}
			
		});
	    signinframe.add(t1);
	    signinframe.add(t2);    
	    signinframe.add(l1); 
	    signinframe.add(l2);  
	    signinframe.add(submit); 
	    signinframe.setSize(600,400);  
	    signinframe.setLayout(null);  
	    windowinfo();
	    signinframe.setVisible(true);  
	}
	public void windowinfo()
	{
		signinframe.addWindowListener(new WindowAdapter() {
		      public void windowClosed(WindowEvent e) {
		        System.out.println("signWindow Close Event");
		      }
		    });
	}
	
	public String getstatus()
	{
		return isloggedin;
	}
	public String getusername()
	{
		return uname;
	}
	}
	
	
	public void videoplayframe(String path) {
		JFrame frame2=new JFrame();
		frame2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		//frame2.setContentPane(mediaPlayerComponent);
		//frame2.setLocation(0, 0);
		//frame2.setSize(screenSize.width, screenSize.height);
		//frame2.setVisible(true);
		Canvas c = new Canvas();
        c.setBackground(Color.black);
        JPanel p = new JPanel();
        c.setBounds(100, 500, 1050, 500);
        p.setLayout(new BorderLayout());
        p.add(c, BorderLayout.CENTER);
        p.setBounds(100, 50, 1050, 600);
        frame2.add(p, BorderLayout.NORTH);
        JPanel p1 = new JPanel();
        p1.setBounds(100, 900, 105, 200);
        frame2.add(p1, BorderLayout.SOUTH);
        JButton playbutton = new JButton("play");
        playbutton.setBounds(50, 50, 150, 100);;
        p1.add(playbutton);
        JButton pausebutton = new JButton("pause");
        pausebutton.setBounds(80, 50, 150, 100);
        p1.add(pausebutton);
        JLabel time=new JLabel();
        
        frame2.setLocation(100, 100);
        frame2.setSize(1050, 600);
        frame2.setVisible(true);
        JButton rewindButton = new JButton("Rewind");
        p1.add(rewindButton);
        rewindButton.setBounds(110, 50, 150, 100);
        JButton skipButton = new JButton("Skip");
        p1.add(skipButton);
        skipButton.setBounds(140, 50, 150, 100);
        JButton restartButton = new JButton("Restart");
        p1.add(restartButton);
        restartButton.setBounds(170, 50, 150, 100);
        p1.add(time);
        time.setBounds(170, 50, 150, 100);
        mediaPlayerComponent.getMediaPlayer().setVideoSurface(mediaPlayerComponent.getMediaPlayerFactory().newVideoSurface(c));
        mediaPlayerComponent.getMediaPlayer().playMedia(path);//Movie name which want to play
        
        /*Timer timer = new Timer(100, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            	long tt=mediaPlayerComponent.getMediaPlayer().getLength();
            	tt/=1000;
            	long ct=mediaPlayerComponent.getMediaPlayer().getTime();
            	ct/=1000;
                time.setText(ct+" / "+tt);
        }

    });

    timer.start();*/
        class replay{
        	//static int cnt;
        	//static {
        //		cnt=0;
        	//}
        	replay(Timer t){
        		int dialogButton = JOptionPane.YES_NO_OPTION;
        		//if(cnt<1)
        		//{
        		//cnt++;
               	int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Replay video","Video completed",dialogButton);
               	if(dialogResult == JOptionPane.YES_OPTION){
               		//t.cancel();
               		restartButton.doClick();
               		//mediaPlayerComponent.getMediaPlayer().stop();
               		//mediaPlayerComponent.getMediaPlayer().play();
               	//	t.scheduleAtFixedRate(mytask, 0,100);
               	}
               	if(dialogResult == JOptionPane.NO_OPTION){
               		try {
               		//t.cancel();
               		}
               		catch(Exception ex) {
               			ex.printStackTrace();
               		}
               		frame2.dispatchEvent(new WindowEvent(frame2, WindowEvent.WINDOW_CLOSING));
                 	}
        		}
        	//}
        }
        mediaPlayerComponent.getMediaPlayer().parseMedia();
        final long tt=mediaPlayerComponent.getMediaPlayer().getLength()/1000;
    	//mediaPlayerComponent.getMediaPlayer().play();
        Timer t = new Timer();
        //t.scheduleAtFixedRate, 0,100);
         TimerTask mytask=new TimerTask() {
                    @Override
                     public void run() {
                    	long ct=mediaPlayerComponent.getMediaPlayer().getTime();
                    	ct/=1000;
                   // 	System.out.println("tt="+tt+"\tct="+ct);
                        time.setText(ct+" / "+tt);
                        if(ct==tt)
                        {
                        	t.cancel();
                        	mediaPlayerComponent.getMediaPlayer().pause();
                        	new replay(t);

                         
                        	
                        }
                        }
         };
        t.scheduleAtFixedRate(mytask,1500,1000);
        pausebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
            	 mediaPlayerComponent.getMediaPlayer().pause();
            	 
            }
        });
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
            	 //mytask.cancel();
            	try {
            	
            	 mediaPlayerComponent.getMediaPlayer().stop();
            	 Timer t2=new Timer();
            	 TimerTask mytask2=new TimerTask() {
                     @Override
                      public void run() {
                     	long ct=mediaPlayerComponent.getMediaPlayer().getTime();
                     	ct/=1000;
                     	//System.out.println("tt="+tt+"\tct="+ct);
                         time.setText(ct+" / "+tt);
                         if(ct==tt)
                         {
                         	t2.cancel();
                         	mediaPlayerComponent.getMediaPlayer().pause();
                         	

                            new replay(t2);
                         }
                         }
          };
            	 mediaPlayerComponent.getMediaPlayer().play();
            	 t2.scheduleAtFixedRate(mytask2,1500, 1000);
            	
            	}
            	catch(Exception e) {
            		e.printStackTrace();
            	}
            }
        });
        playbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
            	 mediaPlayerComponent.getMediaPlayer().play();
            }
        });
       
        rewindButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	 mediaPlayerComponent.getMediaPlayer().parseMedia();
             	//long tt= mediaPlayerComponent.getMediaPlayer().getLength();
             	long ct= mediaPlayerComponent.getMediaPlayer().getTime();
             	System.out.println("tt="+tt+"\tct="+ct);
             	if(ct>10000)
                mediaPlayerComponent.getMediaPlayer().skip(-10000);
             	else
             		mediaPlayerComponent.getMediaPlayer().setTime(0);
            }
        });
        skipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	 mediaPlayerComponent.getMediaPlayer().parseMedia();
            	long tt= mediaPlayerComponent.getMediaPlayer().getLength();
            	long ct= mediaPlayerComponent.getMediaPlayer().getTime();
            	System.out.println("tt="+tt+"\tct="+ct);
            	if(tt-ct>10000)
                mediaPlayerComponent.getMediaPlayer().skip(10000);
            	else
            		 mediaPlayerComponent.getMediaPlayer().setTime(tt);
            }
        });
		frame2.addWindowListener(new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		    	//mediaPlayerComponent.getMediaPlayer().pause();
		    	mediaPlayerComponent.release();
		    }
		});
	}
}
class search{
	int id;
	static int search_count;
	static {
		search_count=0;
	}
	String type;
	search(){
		
	}
	search(int i,String t){
		search_count++;
		id=i;
		type=t;
	}
}
class channel{
	static int channel_count;
	static {
		channel_count=0;
	}
	channel(){
		
	}
	channel(String name,String desc,int accid){
		account_id=accid;
		channel_id=channel_count;
		channel_name=name;
		description=desc;
		channel_count++;
	}
	int channel_id,account_id;
	String channel_name,description;
	int sub_count;
	video vids[]=new video[10];
}
class video{
	static int video_count;
	static {
		video_count=0;
	}
	int video_id;
	String vid_path;
	String vid_name,description;
	String thumb_path;
	int likes,dislikes;
	int channel_id;
	video(){
		
	}
	video(String vname,String desc,String vpath,int cid){
		vid_path=vpath;
		vid_name=vname;
		description=desc;
		channel_id=cid;
		video_id=video_count;
		video_count++;
	}
	//comments comlist[]=new comments[10];
	
}
class comments{
	int comment_id;
	String comment;
	int account_id;
}
class account{
	static int account_count;
	static {
		account_count=0;
	}
	int account_id;
	String gmailid;
	String password;
	//int history[];
	boolean havechannel;
	//int subscriptions[];
	account(){
		
	}
	
	account(String gid,String pwd){
		gmailid=gid;
		password=pwd;
		account_id=account_count;
		havechannel=false;
		account_count++;
	}
	
	boolean validate_crid(String a,String b)
	{
		if(gmailid.equals(a) && password.equals(b) ) {
			return true;
		}
		else {
			return false;
		}
	}
}