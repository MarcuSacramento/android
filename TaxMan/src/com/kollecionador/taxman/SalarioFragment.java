package com.kollecionador.taxman;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kollecionador.taxman.util.Calculos;
import com.kollecionador.taxman.util.FaixaSalarial;
import com.kollecionador.taxman.util.FieldSalario;
import com.kollecionador.taxman.util.ResultadoFaixaSalarial;

public class SalarioFragment extends Fragment {

	List<FaixaSalarial> INSS;
	List<FaixaSalarial> IRRF;
	ResultadoFaixaSalarial INSSResultado;
	ResultadoFaixaSalarial IRRFResultado;
	Float prefBenAlimentacao;
	Float prefBenCombustivel;
	Float prefBenOutros;
	Float prefBenAjuda;
	Float prefPercFGTS;

	SharedPreferences preferences;

	TextView txtINSSSalarioRef;
	TextView txtINSSFeriasRef;
	TextView txtINSSDecimoRef;

	TextView txtIRRFSalarioRef;
	TextView txtIRRFFeriasRef;
	TextView txtIRRFDecimoRef;

	TextView txtINSSSalarioTotal;
	TextView txtINSSFeriasTotal;
	TextView txtINSSDecimoTotal;

	TextView txtIRRFSalarioTotal;
	TextView txtIRRFFeriasTotal;
	TextView txtIRRFDecimoTotal;

	TextView txtOutrosSalario;
	TextView txtOutrosFerias;
	TextView txtOutrosDecimo;

	TextView txtSalario;
	TextView txtFerias;
	TextView txtDecimo;

	TextView txtSalarioProvento;
	TextView txtFeriasProvento;
	TextView txtDecimoProvento;
	TextView txtFGTSProvento;

	TextView txtSalarioDesconto;
	TextView txtFeriasDesconto;
	TextView txtDecimoDesconto;
	TextView txtFGTSDesconto;

	TextView txtProventoAnual;
	TextView txtDescontoAnual;
	TextView txtTotalAnual;
	TextView txtTotalAnualDireito;

	Button btnCalcular;

	FieldSalario salarioCLT;
	EditText diasFerias;
	EditText meses;

	View rootView;

	Calculos calc;
	private AlertDialog.Builder builder;
	private AlertDialog alert;
	boolean campoNulo = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater
				.inflate(R.layout.fragment_salario, container, false);
		builder = new AlertDialog.Builder(rootView.getContext());
		preferences = PreferenceManager.getDefaultSharedPreferences(rootView
				.getContext());
		loadPreferences();
		loadFields();

		calc = new Calculos();

		btnCalcular.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Float.valueOf(salarioCLT.getValue()).compareTo(
						Float.valueOf("0.0")) < 1) {
					builder.setMessage(
							"Não é permitido simular com Salário igual a Zero!")
							.setTitle("Atenção!");
					builder.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

								}
							});
					campoNulo = true;

					alert = builder.create();
					alert.show();
				}

				if (meses.getText().toString().equals("")
						&& diasFerias.getText().toString().equals("")) {
					builder.setMessage(
							"Não é permitido simular com Dias ou Meses Vazios!")
							.setTitle("Atenção!");
					builder.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

								}
							});
					campoNulo = true;

					alert = builder.create();
					alert.show();
				}

				if (!campoNulo)
					preencheTabela();

				campoNulo = false;

			}
		});

		return rootView;
	}

	public void loadPreferences() {
		INSS = new ArrayList<FaixaSalarial>();
		IRRF = new ArrayList<FaixaSalarial>();

		INSS.add(new FaixaSalarial(new Float("0.00"), new Float(preferences
				.getString("inss_faixa_1", "0.00").replace(",", ".")),
				new Float(preferences.getString("inss_percentual_1", "0.00")
						.replace(",", ".")), new Float("0.00")));

		INSS.add(new FaixaSalarial(new Float(preferences.getString(
				"inss_faixa_1", "0.00").replace(",", ".")),
				new Float(preferences.getString("inss_faixa_2", "0.00")
						.replace(",", ".")), new Float(preferences.getString(
						"inss_percentual_2", "0.00").replace(",", ".")),
				new Float("0.00")));

		INSS.add(new FaixaSalarial(new Float(preferences.getString(
				"inss_faixa_2", "0.00").replace(",", ".")),
				new Float(preferences.getString("inss_faixa_3", "0.00")
						.replace(",", ".")), new Float(preferences.getString(
						"inss_percentual_3", "0.00").replace(",", ".")),
				new Float("0.00")));

		INSS.add(new FaixaSalarial(new Float(preferences.getString(
				"inss_faixa_3", "0.00").replace(",", ".")),
				new Float(preferences.getString("inss_faixa_3", "0.00")
						.replace(",", ".")), new Float(preferences.getString(
						"inss_percentual_teto", "0.00").replace(",", ".")),
				new Float("0.00")));

		//
		IRRF.add(new FaixaSalarial(new Float("0.00"), new Float(preferences
				.getString("irrf_faixa_1", "0.00").replace(",", ".")),
				new Float(preferences.getString("irrf_percentual_1", "0.00")
						.replace(",", ".")), new Float(preferences.getString(
						"irrf_deducao_1", "0.00").replace(",", "."))));

		IRRF.add(new FaixaSalarial(new Float(preferences.getString(
				"irrf_faixa_1", "0.00").replace(",", ".")),
				new Float(preferences.getString("irrf_faixa_2", "0.00")
						.replace(",", ".")), new Float(preferences.getString(
						"irrf_percentual_2", "0.00").replace(",", ".")),
				new Float(preferences.getString("irrf_deducao_2", "0.00")
						.replace(",", "."))));

		IRRF.add(new FaixaSalarial(new Float(preferences.getString(
				"irrf_faixa_2", "0.00").replace(",", ".")),
				new Float(preferences.getString("irrf_faixa_3", "0.00")
						.replace(",", ".")), new Float(preferences.getString(
						"irrf_percentual_3", "0.00").replace(",", ".")),
				new Float(preferences.getString("irrf_deducao_3", "0.00")
						.replace(",", "."))));

		IRRF.add(new FaixaSalarial(new Float(preferences.getString(
				"irrf_faixa_3", "0.00").replace(",", ".")),
				new Float(preferences.getString("irrf_faixa_4", "0.00")
						.replace(",", ".")), new Float(preferences.getString(
						"irrf_percentual_4", "0.00").replace(",", ".")),
				new Float(preferences.getString("irrf_deducao_4", "0.00")
						.replace(",", "."))));

		IRRF.add(new FaixaSalarial(new Float(preferences.getString(
				"irrf_faixa_4", "0.00").replace(",", ".")), new Float(
				"1000000.00"), new Float(preferences.getString(
				"irrf_percentual_5", "0.00").replace(",", ".")), new Float(
				preferences.getString("irrf_deducao_5", "0.00").replace(",",
						"."))));

		prefBenAlimentacao = Float.valueOf(preferences.getString(
				"beneficio_alimentacao", "0.00").replace(",", "."));
		prefBenCombustivel = Float.valueOf(preferences.getString(
				"beneficio_combustivel", "0.00").replace(",", "."));
		prefBenOutros = Float.valueOf(preferences.getString("beneficio_outros",
				"0.00").replace(",", "."));

		prefBenAjuda = Float.valueOf(preferences.getString("beneficio_ajuda",
				"0.00").replace(",", "."));

		prefPercFGTS = (Float.valueOf(preferences.getString("fgts_percentual",
				"0.00").replace(",", "."))) / 100;

	}

	public void loadFields() {
		salarioCLT = (FieldSalario) rootView.findViewById(R.id.fieldSalCLT);
		btnCalcular = (Button) rootView.findViewById(R.id.btnCalcular);

		diasFerias = (EditText) rootView.findViewById(R.id.fieldDias);
		meses = (EditText) rootView.findViewById(R.id.fieldmeses);
		txtINSSSalarioRef = (TextView) rootView
				.findViewById(R.id.tvRefINSSSalario);
		txtINSSFeriasRef = (TextView) rootView
				.findViewById(R.id.tvRefINSSFerias);
		txtINSSDecimoRef = (TextView) rootView
				.findViewById(R.id.tvRefINSSDecimo);

		txtIRRFSalarioRef = (TextView) rootView
				.findViewById(R.id.tvRefIRRFSalario);
		txtIRRFFeriasRef = (TextView) rootView
				.findViewById(R.id.tvRefIRRFFerias);
		txtIRRFDecimoRef = (TextView) rootView
				.findViewById(R.id.tvRefIRRFDecimo);

		txtINSSSalarioTotal = (TextView) rootView
				.findViewById(R.id.tvDescontoINSSSalario);
		txtINSSFeriasTotal = (TextView) rootView
				.findViewById(R.id.tvDescontoINSSFerias);
		txtINSSDecimoTotal = (TextView) rootView
				.findViewById(R.id.tvDescontoINSSDecimo);

		txtIRRFSalarioTotal = (TextView) rootView
				.findViewById(R.id.tvDescontoIRRFSalario);
		txtIRRFFeriasTotal = (TextView) rootView
				.findViewById(R.id.tvDescontoIRRFFerias);
		txtIRRFDecimoTotal = (TextView) rootView
				.findViewById(R.id.tvDescontoIRRFDecimo);

		txtOutrosSalario = (TextView) rootView
				.findViewById(R.id.tvOutrosProventoSalario);
		txtOutrosFerias = (TextView) rootView
				.findViewById(R.id.tvOutrosProventoFerias);
		txtOutrosDecimo = (TextView) rootView
				.findViewById(R.id.tvOutrosProventoDecimo);

		txtSalario = (TextView) rootView.findViewById(R.id.tvSalarioLiquido);
		txtFerias = (TextView) rootView.findViewById(R.id.tvFeriasLiquido);
		txtDecimo = (TextView) rootView.findViewById(R.id.tvDecimoLiquido);

		txtSalarioProvento = (TextView) rootView
				.findViewById(R.id.tvTotaisProventoSalario);
		txtFeriasProvento = (TextView) rootView
				.findViewById(R.id.tvTotaisProventoFerias);
		txtDecimoProvento = (TextView) rootView
				.findViewById(R.id.tvTotaisProventoDecimo);

		txtSalarioDesconto = (TextView) rootView
				.findViewById(R.id.tvTotaisDescontoSalario);
		txtFeriasDesconto = (TextView) rootView
				.findViewById(R.id.tvTotaisDescontoFerias);
		txtDecimoDesconto = (TextView) rootView
				.findViewById(R.id.tvTotaisDescontoDecimo);

		txtProventoAnual = (TextView) rootView
				.findViewById(R.id.tvTotaisProventoAnual);
		txtDescontoAnual = (TextView) rootView
				.findViewById(R.id.tvTotaisDescontoAnual);
		txtTotalAnual = (TextView) rootView.findViewById(R.id.liquidoAnual);
		txtFGTSProvento = (TextView) rootView.findViewById(R.id.tvFGTSProvento);
		txtTotalAnualDireito = (TextView) rootView
				.findViewById(R.id.liquidoAnualDireito);

	}

	public void preencheTabela() {

		NumberFormat formato = NumberFormat.getCurrencyInstance();
		NumberFormat formatoP = NumberFormat.getPercentInstance();
		formato.setMaximumFractionDigits(2);
		formatoP.setMaximumFractionDigits(2);

		Float salario = Float.valueOf(salarioCLT.getValue());
		INSSResultado = calc.calculaFaixaINSS(salario, INSS);
		IRRFResultado = calc.calculaFaixaIRRF(
				salario - INSSResultado.getValor(), IRRF);

		txtINSSSalarioRef
				.setText(formatoP.format(INSSResultado.getPercentual() / 100));

		txtIRRFSalarioRef
				.setText(formatoP.format(IRRFResultado.getPercentual() / 100));

		txtINSSSalarioTotal.setText(formato.format(INSSResultado.getValor()));
		txtIRRFSalarioTotal.setText(formato.format(IRRFResultado.getValor()));
		txtOutrosSalario.setText(formato.format(salario));

		Float totalProventoSalario = salario;
		Float totalDescontoSalario = INSSResultado.getValor()
				+ IRRFResultado.getValor();
		txtSalario.setText(formato.format(totalProventoSalario
				- totalDescontoSalario));
		txtSalarioProvento.setText(formato.format(totalProventoSalario));
		txtSalarioDesconto.setText(formato.format(totalDescontoSalario));

		Integer dias = Integer.parseInt(diasFerias.getText().toString());

		Float salarioFerias = (salario / 30);
		salarioFerias = salarioFerias * dias;
		Float terco = salarioFerias / 3;

		INSSResultado = calc.calculaFaixaINSS(salarioFerias + terco, INSS);
		IRRFResultado = calc.calculaFaixaIRRF((salarioFerias + terco)
				- INSSResultado.getValor(), IRRF);

		txtINSSFeriasRef
				.setText(formatoP.format(INSSResultado.getPercentual() / 100));

		txtIRRFFeriasRef
				.setText(formatoP.format(IRRFResultado.getPercentual() / 100));

		txtINSSFeriasTotal.setText(formato.format(INSSResultado.getValor()));
		txtIRRFFeriasTotal.setText(formato.format(IRRFResultado.getValor()));
		txtOutrosFerias.setText(formato.format(salarioFerias) + "+"
				+ formato.format(terco));

		Float totalProventoFerias = salarioFerias + terco;
		Float totalDescontoFerias = INSSResultado.getValor()
				+ IRRFResultado.getValor();
		txtFerias.setText(formato.format(totalProventoFerias
				- totalDescontoFerias));

		txtFeriasProvento.setText(formato.format(totalProventoFerias));
		txtFeriasDesconto.setText(formato.format(totalDescontoFerias));

		Float mesesAno = Float.parseFloat(meses.getText().toString());
		Float salarioDecimo = salario * (mesesAno / 12);
		INSSResultado = calc.calculaFaixaINSS(salarioDecimo, INSS);
		IRRFResultado = calc.calculaFaixaIRRF(
				salarioDecimo - INSSResultado.getValor(), IRRF);

		txtINSSDecimoRef
				.setText(formatoP.format(INSSResultado.getPercentual() / 100));

		txtIRRFDecimoRef
				.setText(formatoP.format(IRRFResultado.getPercentual() / 100));

		txtINSSDecimoTotal.setText(formato.format(INSSResultado.getValor()));
		txtIRRFDecimoTotal.setText(formato.format(IRRFResultado.getValor()));
		txtOutrosDecimo.setText(formato.format(salarioDecimo));

		Float totalProventoDecimo = salarioDecimo;
		Float totalDescontoDecimo = INSSResultado.getValor()
				+ IRRFResultado.getValor();
		txtDecimo.setText(formato.format(totalProventoDecimo
				- totalDescontoDecimo));
		txtDecimoProvento.setText(formato.format(totalProventoDecimo));
		txtDecimoDesconto.setText(formato.format(totalDescontoDecimo));

		txtProventoAnual.setText(formato.format((totalProventoSalario * 11)
				+ totalProventoFerias + totalProventoDecimo));
		txtDescontoAnual.setText(formato.format((totalDescontoSalario * 10)
				+ totalDescontoFerias + totalDescontoDecimo));
		txtTotalAnual
				.setText(formato
						.format(((totalProventoSalario * 11)
								+ totalProventoFerias + totalProventoDecimo)
								- ((totalDescontoSalario * 10)
										+ totalDescontoFerias + totalDescontoDecimo)));

		txtFGTSProvento.setText(formato.format(((Float.valueOf(salarioCLT
				.getValue()) * prefPercFGTS)) * mesesAno));

		txtTotalAnualDireito
				.setText(formato.format((((Float.valueOf(salarioCLT.getValue()) * prefPercFGTS)) * mesesAno)
						+ (prefBenAlimentacao * mesesAno)
						+ (prefBenCombustivel * mesesAno)
						+ (prefBenOutros * mesesAno)
						+ (prefBenAjuda * mesesAno)
						+ (((totalProventoSalario * 11) + totalProventoFerias + totalProventoDecimo)
						- ((totalDescontoSalario * 10) + totalDescontoFerias + totalDescontoDecimo))));

	}
}