package browser;

import history.History;
import history.HistoryBar;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class Browser extends JFrame implements Observer {

	public static final String LINK_OPEN_STRING = "Open ";

	public static final String LINK_DEFAULT_STRING = " ";

	public static final String FAILURE_URL = "file:Failure.html";

	private JEditorPane pane;

	private HistoryBar toolbar;

	private History<String> history;

	// label to show links
	private JLabel label;

	/**
	 * The constructor runs the browser. It displays the main frame with the
	 * fetched initialPage
	 * 
	 * @param initialPage
	 *            the first page to show
	 */
	public Browser(String initialPage) {

		// set up the editor pane
		pane = new JEditorPane();
		pane.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(pane);

		// label to show links
		label = new JLabel(LINK_DEFAULT_STRING);

		pane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent evt) {
				// Mouse enters on the link
				if (evt.getEventType() == HyperlinkEvent.EventType.ENTERED)
					label.setText(LINK_OPEN_STRING + evt.getURL().toString());
				// Link activate
				if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					String selectedURL = evt.getURL().toString();
					history.add(selectedURL);
				}
				// Mouse exits the link
				if (evt.getEventType() == HyperlinkEvent.EventType.EXITED)
					label.setText(LINK_DEFAULT_STRING);
			}
		});

		history = new History<String>();
		history.addObserver(this);
		toolbar = new HistoryBar(history);
		history.add(initialPage);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set up the toolbar and scrollbar in the contentpane of the frame
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(toolbar, BorderLayout.NORTH);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(label, BorderLayout.SOUTH);
		setContentPane(contentPane);
		pack();
		setSize(960, 720);
		setVisible(true);
	}

	public void update(Observable observable, Object arg) {
		if (!observable.equals(history)) {
			return;
		}
		setPage(history.get());
	}

	protected void setPage(String url) {
		try {
			pane.setPage(url);
		} catch (Exception e) {
			try {
				pane.setPage(FAILURE_URL);
			} catch (Exception e2) {
				System.err.println(e2.getMessage());
			}
		}
	}

	/** Create a Browser object. Use the command-line url if given */
	public static void main(String[] args) {
		String initialPage = "http://www.google.com/";
		if (args.length > 0)
			initialPage = args[0];
		new Browser(initialPage);
	}
}
