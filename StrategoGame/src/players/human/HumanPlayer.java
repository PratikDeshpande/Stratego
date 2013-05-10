package players.human;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import stratego_engine.GameState;
import stratego_engine.PieceAction;
import stratego_engine.Player;

public class HumanPlayer extends Player {

	private JFrame humanFrame;
	private JPanel optionsPanel;
	private JComboBox optionsBox;
	private JButton button;
	private boolean buttonClicked;
	private PieceAction selectedAction;
	
	public HumanPlayer(int playerID) {
		super(playerID);
		// TODO Auto-generated constructor stub
	}

	@Override
	public PieceAction nextMove(GameState gs) {
		
		ArrayList<PieceAction> legalActions = gs.getLegalActions(this.playerID);
		
		// to make sure a null value is not returned
		this.selectedAction=legalActions.get(0);
		
		if(!initialized){
			
			initialized=true;
			
			humanFrame = new JFrame("Human Player");
			humanFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			humanFrame.setSize(new Dimension(500,400));

			
			optionsPanel = new JPanel();
		//	optionsPanel.setSize(new Dimension(400,400));
		//	optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
			
			
			//legalActions.listIterator()
			optionsBox = new JComboBox(legalActions.toArray());
		//	optionsBox.add
			
			button = new JButton("Select");
			button.addActionListener(new SelectListener(this));
			
			optionsPanel.add(optionsBox);
			optionsPanel.add(button);
			
			buttonClicked=false;
			
			humanFrame.getContentPane().add(optionsPanel);
			
			humanFrame.setVisible(true);
			
			
		}
		
		optionsBox.removeAllItems();
		for(PieceAction pa:legalActions){
			optionsBox.addItem(pa);
		}
		
		
		while(!this.buttonClicked){
			try {
				Thread.sleep(1000);		
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.buttonClicked=false;
		// Have a timer here
	/*	long start = System.currentTimeMillis();
		long current = System.currentTimeMillis();
		int t=0;
		System.out.println("Before");
		while(((current-start)<(1000*60*5))&&(!this.buttonClicked)){
			current = System.currentTimeMillis();
			//System.out.println(" ");
			t++;
		}
		System.out.println("After");
		System.out.println(t);

		//System.out.println(start);


		//System.out.println(this.buttonClicked);

		// Pick the selected Action 
		System.out.println(this.selectedAction);
		//System.out.println("Here"); */
//		System.out.println(this.selectedAction);

		return this.selectedAction;
	}

	
	private class SelectListener implements ActionListener{

		private HumanPlayer humanPlayer;
		public SelectListener(HumanPlayer humanPlayer) {
			// TODO Auto-generated constructor stub
			this.humanPlayer=humanPlayer;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
			humanPlayer.selectedAction= (PieceAction)humanPlayer.optionsBox.getSelectedItem();
			humanPlayer.buttonClicked=true;
			
			//System.out.println(humanPlayer.buttonClicked);
		}
		
	}
	
}
