package pl.socewicz.kluk.meteo;

import java.io.IOException;
import java.text.ParseException;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

public class MainWindow extends ApplicationWindow {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	static int TYPE = 0;
	
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
		scrolledComposite.setBounds(10, 177, 414, 250);
		formToolkit.adapt(scrolledComposite);
		formToolkit.paintBordersFor(scrolledComposite);
		
		Button btnSync = new Button(container, SWT.NONE);
		btnSync.setBounds(109, 10, 231, 60);
		formToolkit.adapt(btnSync, true, true);
		btnSync.setText("Zbierz dane");
		
		Button btnVisualize = new Button(container, SWT.NONE);
		btnVisualize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LineChartTemp tempChart;
				try {
					tempChart = new LineChartTemp("Wykres - Temperatura", TYPE);
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
		btnVisualize.setBounds(48, 124, 80, 47);
		formToolkit.adapt(btnVisualize, true, true);
		
		Button btnWiatr = new Button(container, SWT.NONE);
		btnWiatr.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LineChartWind tempChart;
				try {
					tempChart = new LineChartWind("Wykres - Wiatr", TYPE);
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
		btnWiatr.setBounds(226, 124, 80, 47);
		formToolkit.adapt(btnWiatr, true, true);
		
		Button btnCi = new Button(container, SWT.NONE);
		btnCi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LineChartPressure tempChart;
				try {
					tempChart = new LineChartPressure("Wykres - Ci\u015Bnienie", TYPE);
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
		btnCi.setBounds(312, 124, 80, 47);
		formToolkit.adapt(btnCi, true, true);
		
		Button btnOpad = new Button(container, SWT.NONE);
		btnOpad.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LineChartFall tempChart;
				try {
					tempChart = new LineChartFall("Wykres - Opad", TYPE);
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
		btnOpad.setBounds(139, 124, 81, 47);
		formToolkit.adapt(btnOpad, true, true);
		
		Button btnDniowa = new Button(container, SWT.RADIO);
		btnDniowa.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TYPE = 0;
			}
		});
		btnDniowa.setBounds(48, 76, 90, 16);
		formToolkit.adapt(btnDniowa, true, true);
		btnDniowa.setText("3 dniowa");
		
		Button btnDniowa_1 = new Button(container, SWT.RADIO);
		btnDniowa_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TYPE = 1;
			}
		});
		btnDniowa_1.setBounds(175, 76, 90, 16);
		formToolkit.adapt(btnDniowa_1, true, true);
		btnDniowa_1.setText("7 dniowa");
		
		Button btnRadioButton = new Button(container, SWT.RADIO);
		btnRadioButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TYPE = 2;
			}
		});
		btnRadioButton.setBounds(302, 76, 90, 16);
		formToolkit.adapt(btnRadioButton, true, true);
		btnRadioButton.setText("14 dniowa");
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
