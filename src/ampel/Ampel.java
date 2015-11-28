package ampel;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Diese Klasse ist die Hauptklasse der Ampel, welche sowohl Grafik als auch interne Berechnungen umfasst.
 * 
 * @author Lukas Schramm
 * @version 1.0
 *
 */
public class Ampel {
	
	private JFrame frame1 = new JFrame("Ampelschaltung");
	private JPanel ampelflaeche = new JPanel() {
		@Override
		public void paintComponent(Graphics gr) {
			super.paintComponent(gr);
			gr.setColor(Color.black);
			int canx = this.getWidth()/2;
			int cany = this.getHeight()/2-12;
			int durchmesser;
			if(this.getHeight()/4>this.getWidth()) {
				durchmesser = this.getWidth();
			} else {
				durchmesser = this.getHeight()/4;
			}
			gr.drawOval(canx-durchmesser/2, cany-durchmesser/2-durchmesser-20, durchmesser, durchmesser);
			gr.drawOval(canx-durchmesser/2, cany-durchmesser/2, durchmesser, durchmesser);
			gr.drawOval(canx-durchmesser/2, cany-durchmesser/2+durchmesser+20, durchmesser, durchmesser);
			
			switch(ampelstatus) {
			case 1:
				gr.setColor(Color.red);
				gr.drawOval(canx-durchmesser/2, cany-durchmesser/2-durchmesser-20, durchmesser, durchmesser);
				gr.fillOval(canx-durchmesser/2, cany-durchmesser/2-durchmesser-20, durchmesser, durchmesser);
		  		break;
		  	case 2:
		  		gr.setColor(Color.red);
		  		gr.drawOval(canx-durchmesser/2, cany-durchmesser/2-durchmesser-20, durchmesser, durchmesser);
		  		gr.fillOval(canx-durchmesser/2, cany-durchmesser/2-durchmesser-20, durchmesser, durchmesser);
		  		gr.setColor(Color.yellow);
		  		gr.drawOval(canx-durchmesser/2, cany-durchmesser/2, durchmesser, durchmesser);
		  		gr.fillOval(canx-durchmesser/2, cany-durchmesser/2, durchmesser, durchmesser);
		  		break;
		  	case 3:
		  		gr.setColor(Color.green);
		  		gr.drawOval(canx-durchmesser/2, cany-durchmesser/2+durchmesser+20, durchmesser, durchmesser);
		  		gr.fillOval(canx-durchmesser/2, cany-durchmesser/2+durchmesser+20, durchmesser, durchmesser);
		  		break;
		  	case 4:
		  		gr.setColor(Color.yellow);
		  		gr.drawOval(canx-durchmesser/2, cany-durchmesser/2, durchmesser, durchmesser);
		  		gr.fillOval(canx-durchmesser/2, cany-durchmesser/2, durchmesser, durchmesser);
		  		break;
		  	case 6:
		  		if(warnstatus == 0) {
		  			gr.setColor(Color.yellow);
			  		gr.drawOval(canx-durchmesser/2, cany-durchmesser/2, durchmesser, durchmesser);
			  		gr.fillOval(canx-durchmesser/2, cany-durchmesser/2, durchmesser, durchmesser);
		  		}
		  		break;
		  	default:break;
		  	}
		}
	};
	private Button buttonautomatik = new Button();
	private Button buttonwarnung = new Button();
	private Button buttonweiter = new Button();
	private int ampelstatus = 0;
	private int warnstatus = 0;
	/**0=Nichts; 1=Normalschaltung, 2=Warnmodus*/
	private int modus = 0;
	private Thread thread = null;
	
	public Ampel() {
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setPreferredSize(new Dimension(400,550));
		frame1.setMinimumSize(new Dimension(350,350));
		frame1.setResizable(true);
		Container cp = frame1.getContentPane();
		cp.setLayout(new GridBagLayout());
		
		buttonautomatik.setLabel("Automatik ein");
		buttonautomatik.setVisible(true);
		buttonautomatik.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				buttonAutomatik_ActionPerformed();
			}
		});
		buttonwarnung.setLabel("Warnmodus ein");
		buttonwarnung.setVisible(true);
		buttonwarnung.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				buttonWarnung_ActionPerformed();
			}
		});
		buttonweiter.setLabel("Weiter schalten");
		buttonweiter.setVisible(true);
		buttonweiter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				buttonWeiter_ActionPerformed();
			}
		});
        
        JPanel rechteflaeche = new JPanel();
        rechteflaeche.setLayout(new BorderLayout());
        JPanel rfAufnahme1 = new JPanel();
        rfAufnahme1.setLayout(new BorderLayout());
        JPanel rfAufnahme2 = new JPanel();
        rfAufnahme1.add(rfAufnahme2,BorderLayout.EAST);
        rfAufnahme2.setLayout(new GridBagLayout());
        rechteflaeche.add(rfAufnahme1,BorderLayout.NORTH);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        c.weightx = 1;
        c.gridy = 0;
        rfAufnahme2.add(buttonautomatik,c);
        c.gridy = 1;
        rfAufnahme2.add(buttonwarnung,c);
        c.gridy = 2;
        rfAufnahme2.add(buttonweiter,c);
		
        frame1.addWindowListener(new WindowAdapter() { 
        	@SuppressWarnings("deprecation")
			public void windowClosed(WindowEvent evt) { 
        		if(thread!=null) {
        			thread.stop();
		  		    thread=null;
		  		}
		    }
		});
        frame1.add(ampelflaeche,new GridBagFelder(0,0,1,1,0.65,1));
		frame1.add(rechteflaeche,new GridBagFelder(1,0,1,1,0.35,1));
		ampelflaeche.setPreferredSize(new Dimension(0,0));
		rechteflaeche.setPreferredSize(new Dimension(0,0));
		frame1.pack();
		frame1.setLocationRelativeTo(null);
		frame1.setVisible(true);
	}
	
	/**
	 * Diese Methode setzt bei Klick auf Automatik den Automatikmodus in Gang, sodass die Ampel automatisch die Ampelphasen durchschaltet.<br>
	 * Wenn der Modus aktiviert ist und ein weiterer Klick erfolgt, wird dies wieder beendet.
	 */
	@SuppressWarnings("deprecation")
	private void buttonAutomatik_ActionPerformed() {
		final int weiter = 1;
		if(thread==null || modus!=1) {
			if(ampelstatus == 6) {
				ampelstatus = 1;
			}
			if(thread!=null) {
				thread.stop();
				thread = null;
			}
			thread = new Thread(new Runnable() {
				@SuppressWarnings("static-access")
				@Override
				public void run() {
					while(weiter > 0) {
						if(ampelstatus == 4) {
							ampelstatus = 1;
		        		} else {
		        			ampelstatus++;
		        		}
						try {
							thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						ampelflaeche.repaint();
					  }
				}
			});
			thread.start();
			buttonautomatik.setLabel("Automatik aus");
			buttonwarnung.setLabel("Warnmodus ein");
			buttonweiter.setEnabled(false);
			modus = 1;
		} else {
			thread.stop();
			buttonautomatik.setLabel("Automatik ein");
			buttonweiter.setEnabled(true);
			thread = null;
			modus = 0;
		}
	}
	
	/**
	 * Diese Methode setzt bei Klick auf Warnung den Warnmodus in Gang, sodass die Ampel automatisch gelb blinkt.<br>
	 * Wenn der Modus aktiviert ist und ein weiterer Klick erfolgt, wird dies wieder beendet.
	 */
	@SuppressWarnings("deprecation")
	private void buttonWarnung_ActionPerformed() {
		final int weiter = 1;
		if(thread==null || modus!=2) {
			if(thread!=null) {
				thread.stop();
				thread = null;
			}
			ampelstatus = 6;
			thread = new Thread(new Runnable() {
				@SuppressWarnings("static-access")
				@Override
				public void run() {
					while(weiter > 0) {
						if(warnstatus == 0) {
							warnstatus = 1;
						} else {
							warnstatus = 0;
						}
						try {
							thread.sleep(1000);
						} catch(InterruptedException e) {
							e.printStackTrace();
						}
						ampelflaeche.repaint();
					}
				}
			});
			thread.start();
			buttonautomatik.setLabel("Automatik ein");
			buttonwarnung.setLabel("Warnmodus aus");
			buttonweiter.setEnabled(false);
			modus = 2;
		} else {
			thread.stop();
			buttonwarnung.setLabel("Warnmodus ein");
			buttonweiter.setEnabled(true);
			thread = null;
			modus = 0;
		}
	}
	
	/**
	 * Diese Methode schaltet eine Ampelphase weiter.
	 */
	private void buttonWeiter_ActionPerformed() {
		switch(ampelstatus) {
	  	case 0:
	  		ampelflaeche.repaint();
	  		ampelstatus = 1;
	  		break;
	  	case 1:
	  		ampelflaeche.repaint();
	  		ampelstatus = 2;
	  		break;
	  	case 2:
	  		ampelflaeche.repaint();
	  		ampelstatus = 3;
	  		break;
	  	case 3:
	  		ampelflaeche.repaint();
	  		ampelstatus = 4;
	  		break;
	  	case 4:
	  		ampelflaeche.repaint();
	  		ampelstatus = 1;
	  		break;
	  	case 6:
	  		ampelflaeche.repaint();
	  		ampelstatus = 3;
	  	default:
		}
	}
	
	public static void main(String[] args) {
		new Ampel();
	}
}