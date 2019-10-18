package Interfaz;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class AlgoritmoCalidad {

	private JFrame frmAlgoritmoDeCalidad;
	protected String seleccion = "";
	protected String resultado = "";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AlgoritmoCalidad window = new AlgoritmoCalidad();
					window.frmAlgoritmoDeCalidad.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AlgoritmoCalidad() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAlgoritmoDeCalidad = new JFrame();
		frmAlgoritmoDeCalidad.setTitle("Algoritmo de Calidad");
		frmAlgoritmoDeCalidad.setBounds(100, 100, 450, 300);
		frmAlgoritmoDeCalidad.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 148, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 144, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		frmAlgoritmoDeCalidad.getContentPane().setLayout(gridBagLayout);

		try {
			Scanner s = new Scanner(new File("Opciones.csv"));
			opciones(s.nextLine(), s);
		} catch (Exception e) {
		}
	}

	public void opciones(String text, Scanner scan) {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		frmAlgoritmoDeCalidad.getContentPane().add(panel, gbc_panel);

		JTextArea texto = new JTextArea();
		GridBagConstraints gbc_texto = new GridBagConstraints();
		gbc_texto.insets = new Insets(0, 0, 5, 0);
		gbc_texto.fill = GridBagConstraints.BOTH;
		gbc_texto.gridx = 1;
		gbc_texto.gridy = 1;
		frmAlgoritmoDeCalidad.getContentPane().add(texto, gbc_texto);

		String descripcion = text.substring(0, text.indexOf(";"));
		text = text.substring(descripcion.length() + 1);
//		String item = text.substring(0, text.indexOf(";"));
//		text = text.substring(item.length() + 1);
		texto.setText(descripcion);
		ArrayList<JRadioButton> a = new ArrayList<>();
		for (String s : text.split(";"))
			a.add(boton(s, panel, a.size()));
		evento(a);
		if (scan.hasNext()) {
			JButton ok = new JButton("Siguiente");
			ok.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int index = selecciono(a);
					if (index == -1)
						return;

					frmAlgoritmoDeCalidad.getContentPane().removeAll();
					opciones(scan.nextLine(), scan);
					resultado += 100 * index / a.size() + ";";
				}

			});
			GridBagConstraints gbc_ok = new GridBagConstraints();
			gbc_ok.insets = new Insets(0, 0, 0, 5);
			gbc_ok.gridx = 0;
			gbc_ok.gridy = 2;
			frmAlgoritmoDeCalidad.getContentPane().add(ok, gbc_ok);
		} else {
			JButton fin = new JButton("Finalizar");
			fin.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int index = selecciono(a);
					if (index == -1)
						return;

					frmAlgoritmoDeCalidad.getContentPane().removeAll();
					resultado += 100 * index / a.size();

					try {
						File file = new File("Resultados.csv");
						FileWriter f = new FileWriter(file, true);
						if (!file.exists()) {
							// poner titulos
						} else
							f.append("\r\n");
						f.append(resultado);
						resultado = "";
						f.close();
						Scanner s = new Scanner(new File("Opciones.csv"));
						opciones(s.nextLine(), s);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			GridBagConstraints gbc_ok = new GridBagConstraints();
			gbc_ok.insets = new Insets(0, 0, 0, 5);
			gbc_ok.gridx = 0;
			gbc_ok.gridy = 2;
			frmAlgoritmoDeCalidad.getContentPane().add(fin, gbc_ok);
		}
	}

	private JRadioButton boton(String op1, JPanel panel, int nivel) {
		JRadioButton b1 = new JRadioButton(op1);
		b1.setBounds(6, 7 + (nivel * 26), 109, 23);
		panel.add(b1);
		return b1;
	}

	private void evento(ArrayList<JRadioButton> a) {
		for (JRadioButton b : a)
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (b.isSelected())
						for (JRadioButton b2 : a)
							if (!b2.equals(b))
								b2.setSelected(false);
					seleccion = b.getText();
				}
			});

	}

	private int selecciono(ArrayList<JRadioButton> a) {
		int i = a.size();
		for (JRadioButton b : a) {
			if (b.isSelected())
				return i;
			i--;
		}
		return -1;
	}
}
