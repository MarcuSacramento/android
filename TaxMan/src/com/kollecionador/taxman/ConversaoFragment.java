package com.kollecionador.taxman;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.kollecionador.taxman.util.Calculos;
import com.kollecionador.taxman.util.FaixaSalarial;
import com.kollecionador.taxman.util.FieldSalario;
import com.kollecionador.taxman.util.ResultadoFaixaSalarial;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ConversaoFragment extends Fragment {

	List<FaixaSalarial> INSS;
	List<FaixaSalarial> IRRF;
	ResultadoFaixaSalarial INSSResultado;
	ResultadoFaixaSalarial IRRFResultado;
	Float prefBenAlimentacao;
	Float prefBenCombustivel;
	Float prefBenOutros;
	Float prefBenAjuda;
	Float prefPercFGTS;
	Float prefAliqSimples;
	Float valorComImposto;
	Float valorImposto;
	SharedPreferences preferences;

	View rootView;

	Calculos calc;
	private AlertDialog.Builder builder;
	private AlertDialog alert;
	boolean campoNulo = false;

	FieldSalario salarioCLT;
	Button btnCalcular;
	TextView txtTotalAnualDireito;
	TextView txtTotalImposto;
	TextView txtTotalcomImposto;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_conversao, container,
				false);

		builder = new AlertDialog.Builder(rootView.getContext());
		preferences = PreferenceManager.getDefaultSharedPreferences(rootView
				.getContext());

		builder = new AlertDialog.Builder(rootView.getContext());
		preferences = PreferenceManager.getDefaultSharedPreferences(rootView
				.getContext());
		loadPreferences();
		loadFields();

		calc = new Calculos();

		btnCalcular.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				preencheTabela();

			}
		});

		return rootView;
	}

	public void loadFields() {
		salarioCLT = (FieldSalario) rootView.findViewById(R.id.fieldSalCLT);
		btnCalcular = (Button) rootView.findViewById(R.id.btnCalcular);
		txtTotalAnualDireito = (TextView) rootView
				.findViewById(R.id.liquidoAnualDireito);

		txtTotalImposto = (TextView) rootView
				.findViewById(R.id.liquidoAnualDireitoPJImp);
		txtTotalcomImposto = (TextView) rootView
				.findViewById(R.id.liquidoAnualDireitoPJ);

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

		prefAliqSimples = (Float.valueOf(preferences.getString(
				"simples_nacional", "0.00").replace(",", ".")));

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

		Float totalProventoSalario = salario;
		Float totalDescontoSalario = INSSResultado.getValor()
				+ IRRFResultado.getValor();

		Integer dias = 30;

		Float salarioFerias = (salario / 30);
		salarioFerias = salarioFerias * dias;
		Float terco = salarioFerias / 3;

		INSSResultado = calc.calculaFaixaINSS(salarioFerias + terco, INSS);
		IRRFResultado = calc.calculaFaixaIRRF((salarioFerias + terco)
				- INSSResultado.getValor(), IRRF);

		Float totalProventoFerias = salarioFerias + terco;
		Float totalDescontoFerias = INSSResultado.getValor()
				+ IRRFResultado.getValor();

		Float mesesAno = Float.parseFloat("12.00");
		Float salarioDecimo = salario * (mesesAno / 12);

		INSSResultado = calc.calculaFaixaINSS(salarioDecimo, INSS);
		IRRFResultado = calc.calculaFaixaIRRF(
				salarioDecimo - INSSResultado.getValor(), IRRF);

		Float totalProventoDecimo = salarioDecimo;
		Float totalDescontoDecimo = INSSResultado.getValor()
				+ IRRFResultado.getValor();

		Float fgts = ((salario * prefPercFGTS)) * mesesAno;

		Float liquidoAnual = (((salario * prefPercFGTS)) * mesesAno)
				+ (prefBenAlimentacao * mesesAno)
				+ (prefBenCombustivel * mesesAno)
				+ (prefBenOutros * mesesAno)
				+ (prefBenAjuda * mesesAno)
				+ (((totalProventoSalario * 11) + totalProventoFerias + totalProventoDecimo) - ((totalDescontoSalario * 10)
						+ totalDescontoFerias + totalDescontoDecimo));

		txtTotalAnualDireito.setText(formato.format(liquidoAnual));

		Float aux = liquidoAnual;
		Float incremento = Float.valueOf("5.00");
		Float decremento = Float.valueOf("0.50");
		Float desconto;

		boolean controle = false;

		do {
			aux = aux + incremento;
			desconto = aux * (prefAliqSimples / 100);

			if ((aux - desconto) >= liquidoAnual) {
				controle = true;
			}

		} while (!controle);

		valorImposto = aux * (prefAliqSimples / 100);

		valorComImposto = aux;

		txtTotalImposto.setText(formato.format(valorImposto));
		txtTotalcomImposto.setText(formato.format(valorComImposto));

	}
}