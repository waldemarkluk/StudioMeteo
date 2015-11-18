package pl.socewicz.kluk.meteo;

import java.io.IOException;
import java.text.ParseException;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jfree.ui.RefineryUtilities;

import pl.socewicz.kluk.meteo.charts.LineChartFall;
import pl.socewicz.kluk.meteo.charts.LineChartPressure;
import pl.socewicz.kluk.meteo.charts.LineChartTemp;
import pl.socewicz.kluk.meteo.charts.LineChartWind;
import pl.socewicz.kluk.meteo.listeners.SyncSelectionAdapter;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class MainWindow extends ApplicationWindow {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	/**
	 * Create the application window,
	 */
	public MainWindow() {
		super(null);
		createActions();
		addCoolBar(SWT.FLAT);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(null);
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(container, SWT.V_SCROLL);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setAlwaysShowScrollBars(true);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setBounds(10, 154, 414, 250);
		formToolkit.adapt(scrolledComposite);
		formToolkit.paintBordersFor(scrolledComposite);
		
		TextViewer textViewer = new TextViewer(scrolledComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.READ_ONLY | SWT.WRAP);
		textViewer.setRedraw(true);
		textViewer.setEditable(false);
		StyledText styledText = textViewer.getTextWidget();
		formToolkit.paintBordersFor(styledText);
		scrolledComposite.setContent(styledText);
		scrolledComposite.setMinSize(new Point(414, 250));
		
		Button btnSync = new Button(container, SWT.NONE);
		btnSync.addSelectionListener(new SyncSelectionAdapter(styledText));
		btnSync.setBounds(109, 10, 231, 60);
		formToolkit.adapt(btnSync, true, true);
		btnSync.setText("Zbierz dane");
		
		Button btnVisualize = new Button(container, SWT.NONE);
		btnVisualize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LineChartTemp tempChart;
				try {
					tempChart = new LineChartTemp("Wykres - Temperatura");
					tempChart.pack();
			        RefineryUtilities.centerFrameOnScreen(tempChart);
			        tempChart.setVisible(true);
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				} 
			}
		});
		btnVisualize.setText("Temperatura");
		btnVisualize.setBounds(52, 76, 80, 47);
		formToolkit.adapt(btnVisualize, true, true);
		
		Button btnWiatr = new Button(container, SWT.NONE);
		btnWiatr.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LineChartWind tempChart;
				try {
					tempChart = new LineChartWind("Wykres - Wiatr");
					tempChart.pack();
			        RefineryUtilities.centerFrameOnScreen(tempChart);
			        tempChart.setVisible(true);
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				} 
			}
		});
		btnWiatr.setText("Wiatr");
		btnWiatr.setBounds(225, 76, 80, 47);
		formToolkit.adapt(btnWiatr, true, true);
		
		Button btnCi = new Button(container, SWT.NONE);
		btnCi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LineChartPressure tempChart;
				try {
					tempChart = new LineChartPressure("Wykres - Ci\u015Bnienie");
					tempChart.pack();
			        RefineryUtilities.centerFrameOnScreen(tempChart);
			        tempChart.setVisible(true);
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				} 
			}
		});
		btnCi.setText("Ci\u015Bnienie");
		btnCi.setBounds(311, 76, 80, 47);
		formToolkit.adapt(btnCi, true, true);
		
		Button btnOpad = new Button(container, SWT.NONE);
		btnOpad.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LineChartFall tempChart;
				try {
					tempChart = new LineChartFall("Wykres - Opad");
					tempChart.pack();
			        RefineryUtilities.centerFrameOnScreen(tempChart);
			        tempChart.setVisible(true);
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				} 
			}
		});
		btnOpad.setText("Opad");
		btnOpad.setBounds(138, 76, 81, 47);
		formToolkit.adapt(btnOpad, true, true);
		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("LOL");
		menuManager.setMenuText("LOL");
		return menuManager;
	}

	/**
	 * Create the coolbar manager.
	 * @return the coolbar manager
	 */
	/*@Override
	protected CoolBarManager createCoolBarManager(int style) {
	}*/

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			MainWindow window = new MainWindow();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("R-Meteo");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 500);
	}
}
