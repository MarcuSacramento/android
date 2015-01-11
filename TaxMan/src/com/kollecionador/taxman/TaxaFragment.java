package com.kollecionador.taxman;

import java.text.NumberFormat;

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
import android.widget.TextView;

import com.kollecionador.taxman.util.Calculos;
import com.kollecionador.taxman.util.Calculos.Contratacao;
import com.kollecionador.taxman.util.FieldMargem;
import com.kollecionador.taxman.util.FieldSalario;
import com.kollecionador.taxman.util.Preferencia;
import com.kollecionador.taxman.util.TabelaTaxa;

public class TaxaFragment extends Fragment {

	private AlertDialog.Builder builder;
	private AlertDialog alert;
	String textoEnviar;
	Button btnCalcular;
	Button btnEnviar;
	TextView txtSalarioCLT;
	TextView txtTaxaMinimaCLT;
	TextView txtCustosCLT;
	TextView txtCustosImpostoFaturaCLT;
	TextView txtCustosEncargosCLT;
	TextView txtCustosSalarioCLT;
	TextView txtResultadoMCLT;
	TextView txtResultadoPCLT;
	TextView txtFaturamentoCLT;
	TextView txtSalarioPJ;
	TextView txtTaxaMinimaPJ;
	TextView txtCustosPJ;
	TextView txtCustosImpostoFaturaPJ;
	TextView txtCustosEncargosPJ;
	TextView txtCustosSalarioPJ;
	TextView txtResultadoMPJ;
	TextView txtResultadoPPJ;
	TextView txtFaturamentoPJ;
	TextView txtBeneficio;
	FieldSalario txtTaxaAtual;
	TextView txtResultadoPCLTAtual;
	TextView txtFaturamentoPJAtual;

	TextView txtDiasUteis;
	TextView txtHora;
	TextView txtAlimentacao;
	TextView txtTransporte;
	TextView txtOutros;
	TextView txtEncargo;
	TextView txtImpostoFatura;
	TextView txtdissidio;
	TextView txtAjuda;

	FieldSalario salarioCLT;
	FieldSalario salarioPJ;
	FieldMargem fieldMargem;

	Preferencia preferencia;

	Float prefDiasMes;
	Float prefHorasDia;
	Float prefBenAlimentacao;
	Float prefBenCombustivel;
	Float prefBenOutros;
	Float prefBenAjuda;
	Float prefImpCLT;
	Float prefImpFatura;
	Float prefImpDissidio;
	Float prefMargem;
	Float beneficio;
	Float incremento;

	Float taxaCLT;
	Float taxaPJ;

	SharedPreferences preferences;

	Float teste;
	Calculos calc;
	TabelaTaxa clt;
	TabelaTaxa pj;
	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_taxa, container, false);

		builder = new AlertDialog.Builder(rootView.getContext());
		preferences = PreferenceManager.getDefaultSharedPreferences(rootView
				.getContext());

		loadFields();
		loadPreferences();
		fieldMargem.setValuePreference(preferencia.getPercMargem());

		calc = new Calculos();

		btnCalcular.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				boolean campoNulo = false;
				Float salarioCLTF = Float.valueOf(salarioCLT.getValue());
				Float salarioPJF = Float.valueOf(salarioPJ.getValue());

				if (salarioCLTF.compareTo(Float.valueOf("0.0"))
						+ salarioPJF.compareTo(Float.valueOf("0.0")) < 2) {
					builder.setMessage(
							"Não é permitido simular com Salários iguais a Zero!")
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

				if (Float.valueOf(fieldMargem.getValue()) >= Float
						.valueOf("70.00") && !campoNulo) {
					builder.setMessage(
							"Por segurança o aplicativo limita a margem a no máximo 70.00%. Realizando o Cálculo...")
							.setTitle("Atenção!");
					builder.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									fieldMargem.setText("70,00");
									preencheTabela();
								}
							});
					alert = builder.create();
					alert.show();
				}

				if (!campoNulo) {
					builder.setMessage("Realizando Cálculo...").setTitle(
							"Aguarde");

					alert = builder.create();
					alert.show();
					preencheTabela();
					alert.dismiss();
				}
			}
		});

		btnEnviar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, textoEnviar);
				sendIntent.setType("text/plain");

				startActivity(Intent.createChooser(sendIntent,
						"Enviar Conteúdo..."));

			}

		});

		return rootView;
	}

	public void loadPreferences() {

		NumberFormat formato = NumberFormat.getCurrencyInstance();
		NumberFormat formatoP = NumberFormat.getPercentInstance();
		preferencia = new Preferencia();

		preferencia.setDiasUteis(Float.valueOf(preferences.getString(
				"dias_uteis_mes", "0.00").replace(",", ".")));
		preferencia.setHorasTrabalho(Float.valueOf(preferences.getString(
				"horas_trabalho", "0.00").replace(",", ".")));
		preferencia.setBenAlimentacao(Float.valueOf(preferences.getString(
				"beneficio_alimentacao", "0.00").replace(",", ".")));
		preferencia.setBenCombustivel(Float.valueOf(preferences.getString(
				"beneficio_combustivel", "0.00").replace(",", ".")));
		preferencia.setBenOutros(Float.valueOf(preferences.getString(
				"beneficio_outros", "0.00").replace(",", ".")));
		preferencia.setBenAjudaCusto(Float.valueOf(preferences.getString(
				"beneficio_ajuda", "0.00").replace(",", ".")));
		preferencia.setImpEncargo(Float.valueOf(preferences.getString(
				"encargo_clt", "0.00").replace(",", ".")));
		preferencia.setImpFatura(Float.valueOf(preferences.getString(
				"imposto_fatura", "0.00").replace(",", ".")));
		preferencia.setDissidio(Float.valueOf(preferences.getString("dissidio",
				"0.00").replace(",", ".")));
		preferencia.setPercMargem(Float.valueOf(preferences.getString("margem",
				"0.00").replace(",", ".")));

		txtDiasUteis.setText(preferencia.getDiasUteis().toString());
		txtHora.setText(preferencia.getHorasTrabalho().toString());
		txtAlimentacao.setText(formato.format(preferencia.getBenAlimentacao()));
		txtTransporte.setText(formato.format(preferencia.getBenCombustivel()));
		txtOutros.setText(formato.format(preferencia.getBenOutros()));
		txtEncargo.setText(formatoP.format(preferencia.getImpEncargo() / 100));
		txtImpostoFatura
				.setText(formatoP.format(preferencia.getImpFatura() / 100));
		txtdissidio.setText(formatoP.format(preferencia.getDissidio() / 100));

		txtBeneficio.setText("Benefícios("
				+ formato.format(preferencia.getTotalBeneficios()) + ")");
		txtAjuda.setText(formato.format(preferencia.getBenAjudaCusto()));

	}

	public void loadFields() {

		btnCalcular = (Button) rootView.findViewById(R.id.btnCalcular);
		btnEnviar = (Button) rootView.findViewById(R.id.btnEnviar);
		fieldMargem = (FieldMargem) rootView.findViewById(R.id.fieldMargem);
		salarioCLT = (FieldSalario) rootView.findViewById(R.id.fieldSalCLT);
		salarioPJ = (FieldSalario) rootView.findViewById(R.id.fieldValPJ);
		txtAjuda = (TextView) rootView.findViewById(R.id.tvAjudaV);
		txtAlimentacao = (TextView) rootView.findViewById(R.id.tvAlimentacaoV);
		txtBeneficio = (TextView) rootView.findViewById(R.id.txtBeneficio);
		txtCustosCLT = (TextView) rootView.findViewById(R.id.tvCustosCLT);
		txtCustosEncargosCLT = (TextView) rootView
				.findViewById(R.id.tvEncargoCLT);
		txtCustosEncargosPJ = (TextView) rootView
				.findViewById(R.id.tvEncargoPJ);
		txtCustosImpostoFaturaCLT = (TextView) rootView
				.findViewById(R.id.tvFaturaCLT);
		txtCustosImpostoFaturaPJ = (TextView) rootView
				.findViewById(R.id.tvFaturaPJ);
		txtCustosPJ = (TextView) rootView.findViewById(R.id.tvCustosPJ);
		txtCustosSalarioCLT = (TextView) rootView
				.findViewById(R.id.tvSalarioLabelCLT);
		txtCustosSalarioPJ = (TextView) rootView
				.findViewById(R.id.tvSalarioLabelPJ);
		txtDiasUteis = (TextView) rootView.findViewById(R.id.tvDiaUtilV);
		txtdissidio = (TextView) rootView.findViewById(R.id.tvdissidioV);
		txtEncargo = (TextView) rootView.findViewById(R.id.tvEncargoV);
		txtFaturamentoCLT = (TextView) rootView
				.findViewById(R.id.tvFaturamentoCLT);
		txtFaturamentoPJAtual = (TextView) rootView
				.findViewById(R.id.tvResultadoTPJ);
		txtFaturamentoPJ = (TextView) rootView
				.findViewById(R.id.tvFaturamentoPJ);
		txtHora = (TextView) rootView.findViewById(R.id.tvHoraDiaV);
		txtImpostoFatura = (TextView) rootView
				.findViewById(R.id.tvImpostoFaturaV);
		txtOutros = (TextView) rootView.findViewById(R.id.tvOutrosV);
		txtResultadoMCLT = (TextView) rootView
				.findViewById(R.id.tvResultadoRSCLT);
		txtResultadoMPJ = (TextView) rootView
				.findViewById(R.id.tvResultadoRSPJ);
		txtResultadoPCLTAtual = (TextView) rootView
				.findViewById(R.id.tvResultadoTCLT);
		txtResultadoPCLT = (TextView) rootView
				.findViewById(R.id.tvResultadoPCLT);
		txtResultadoPPJ = (TextView) rootView.findViewById(R.id.tvResultadoPPJ);
		txtSalarioCLT = (TextView) rootView.findViewById(R.id.tvSalBrutoCLT);
		txtSalarioPJ = (TextView) rootView.findViewById(R.id.tvSalBrutoPJ);
		txtTaxaAtual = (FieldSalario) rootView
				.findViewById(R.id.fieldTaxaAtual);
		txtTaxaMinimaCLT = (TextView) rootView.findViewById(R.id.tvTaxaMinCLT);
		txtTaxaMinimaPJ = (TextView) rootView.findViewById(R.id.tvTaxaMinPJ);
		txtTransporte = (TextView) rootView.findViewById(R.id.tvTransporteV);
	}

	public void preencheTabela() {
		NumberFormat formato = NumberFormat.getCurrencyInstance();
		NumberFormat formatoP = NumberFormat.getPercentInstance();
		formatoP.setMaximumFractionDigits(2);

		Float salario;
		Float contrato;
		Float taxaMinCLT;
		Float taxaMinPJ;
		Float taxaCLT;
		Float taxaPJ;

		Float impostoFaturaCLT;
		Float cargaCLT;

		Float impostoFaturaPJ;
		Float faturamentoCLT;
		Float faturamentoPJ;
		Float custoCLT;
		Float custoPJ;
		Float custoCLTTx;
		Float custoPJTx;

		salario = Float.valueOf(salarioCLT.getValue());
		contrato = Float.valueOf(salarioPJ.getValue());

		taxaCLT = calc.calculaTaxa(salario, preferencia.getBeneficios(),
				preferencia.getImpEncargo(), preferencia.getImpFatura(),
				preferencia.getDissidio(),
				Float.valueOf(fieldMargem.getValue()), "CLT");

		taxaPJ = calc.calculaTaxa(contrato, preferencia.getBeneficios(),
				preferencia.getImpEncargo(), preferencia.getImpFatura(),
				preferencia.getDissidio(),
				Float.valueOf(fieldMargem.getValue()), "PJ");

		clt = new TabelaTaxa();
		pj = new TabelaTaxa();

		clt = calc.calculoTabelaTaxa(salario, taxaCLT,
				Float.valueOf(txtTaxaAtual.getValue()), preferencia,
				Contratacao.CONTRATACAO_CLT);

		txtTaxaMinimaCLT.setText(formato.format(clt.getTaxa()));
		txtSalarioCLT.setText(formato.format(clt.getSalario()));
		txtCustosCLT.setText(formato.format(clt.getCustos()));
		txtCustosImpostoFaturaCLT.setText(formato.format(clt.getImposto()));
		txtCustosEncargosCLT.setText(formato.format(clt.getEncargos()));
		txtCustosSalarioCLT.setText(formato.format(clt.getSalario()));
		txtResultadoMCLT.setText(formato.format(clt.getResultadoReal()) + "/"
				+ formato.format(clt.getResultadoRealA()) + "*");
		txtResultadoPCLT.setText(formatoP.format(clt.getResultadoPercentual())
				+ "/" + formatoP.format(clt.getResultadoPercentualA()) + "*");
		txtResultadoPCLTAtual.setText(formatoP.format(clt.getResultadoTxAtual())
				+ "/" + formatoP.format(clt.getResultadoTxAtualA()) + "*");
		txtFaturamentoCLT.setText(formato.format(clt.getFaturamento()));

		pj = calc.calculoTabelaTaxa(contrato, taxaPJ,
				Float.valueOf(txtTaxaAtual.getValue()), preferencia,
				Contratacao.CONTRATACAO_PJ);

		txtTaxaMinimaPJ.setText(formato.format(pj.getTaxa()));
		txtSalarioPJ.setText(formato.format(pj.getSalario()));
		txtCustosPJ.setText(formato.format(pj.getCustos()));
		txtCustosImpostoFaturaPJ.setText(formato.format(pj.getImposto()));
		txtCustosEncargosPJ.setText(formato.format(pj.getEncargos()));
		txtCustosSalarioPJ.setText(formato.format(pj.getSalario()));
		txtResultadoMPJ.setText(formato.format(pj.getResultadoReal()));
		txtResultadoPPJ.setText(formatoP.format(pj.getResultadoPercentual()));
		txtFaturamentoPJAtual.setText(formatoP.format(pj.getResultadoTxAtual()));
		txtFaturamentoPJ.setText(formato.format(pj.getFaturamento()));

		/*
		 * 
		 * taxaMinCLT = taxaCLT / (prefDiasMes * prefHorasDia); taxaMinPJ =
		 * taxaPJ / (prefDiasMes * prefHorasDia); impostoFaturaCLT = taxaMinCLT
		 * * (prefDiasMes * prefHorasDia) (prefImpFatura / 100); cargaCLT =
		 * (Float.valueOf(salarioCLT.getValue()) * (prefImpCLT / 100)) +
		 * (Float.valueOf(salarioCLT.getValue()) * (prefImpDissidio / 100)) +
		 * beneficio;
		 * 
		 * faturamentoCLT = taxaMinCLT * (prefDiasMes * prefHorasDia);
		 * faturamentoPJ = taxaMinPJ * (prefDiasMes * prefHorasDia);
		 * 
		 * impostoFaturaPJ = taxaMinPJ * (prefDiasMes * prefHorasDia)
		 * (prefImpFatura / 100);
		 * 
		 * txtTaxaMinimaCLT.setText(formato.format(taxaMinCLT));
		 * txtTaxaMinimaPJ.setText(formato.format(taxaMinPJ));
		 * 
		 * txtSalarioCLT.setText(formato.format(Float.parseFloat(salarioCLT
		 * .getValue())));
		 * txtSalarioPJ.setText(formato.format(Float.parseFloat(salarioPJ
		 * .getValue())));
		 * 
		 * custoCLT = (impostoFaturaCLT + cargaCLT) +
		 * Float.parseFloat(salarioCLT.getValue());
		 * 
		 * taxaCLT = Float.valueOf(txtTaxaAtual.getValue()) (prefDiasMes *
		 * prefHorasDia);
		 * 
		 * custoCLTTx = ((taxaCLT * (prefImpFatura / 100)) + cargaCLT) +
		 * Float.parseFloat(salarioCLT.getValue());
		 * 
		 * txtCustosCLT.setText(formato.format(custoCLT));
		 * txtCustosImpostoFaturaCLT.setText(formato.format(impostoFaturaCLT));
		 * txtCustosEncargosCLT.setText(formato.format(cargaCLT));
		 * txtCustosSalarioCLT
		 * .setText(formato.format(Float.parseFloat(salarioCLT .getValue())));
		 * 
		 * custoPJ = (impostoFaturaPJ) + Float.parseFloat(salarioPJ.getValue());
		 * 
		 * custoPJTx = (Float.valueOf(txtTaxaAtual.getValue()) (prefDiasMes *
		 * prefHorasDia) * (prefImpFatura / 100)) +
		 * Float.parseFloat(salarioPJ.getValue());
		 * 
		 * txtCustosPJ.setText(formato.format(custoPJ));
		 * txtCustosImpostoFaturaPJ.setText(formato.format(impostoFaturaPJ));
		 * txtCustosEncargosPJ.setText("Não se aplica");
		 * txtCustosSalarioPJ.setText(formato.format(Float.parseFloat(salarioPJ
		 * .getValue())));
		 * 
		 * Float custoAjuda = faturamentoCLT - custoCLT - prefBenAjuda; String
		 * sinal = ""; if (custoAjuda < 0) sinal = " - ";
		 * 
		 * txtResultadoMCLT.setText(formato.format(faturamentoCLT - custoCLT) +
		 * "/" + sinal + formato.format(faturamentoCLT - custoCLT -
		 * prefBenAjuda) + "*");
		 * 
		 * txtResultadoMPJ.setText(formato.format(faturamentoPJ - custoPJ));
		 * formatoP.setMaximumFractionDigits(2); txtResultadoPCLT
		 * .setText(formatoP.format((1 - (custoCLT / faturamentoCLT))) + "/" +
		 * formatoP .format((1 - ((custoCLT + prefBenAjuda) / faturamentoCLT)))
		 * + "*");
		 * 
		 * txtResultadoPPJ.setText(formatoP .format((1 - (custoPJ /
		 * faturamentoPJ))));
		 * 
		 * txtFaturamentoCLT.setText(formato.format(faturamentoCLT));
		 * txtFaturamentoPJ.setText(formato.format(faturamentoPJ));
		 * 
		 * txtResultadoPCLTAtual.setText(formatoP .format((1 - (custoCLTTx /
		 * taxaCLT))) + "/" + formatoP .format((1 - ((custoCLTTx + prefBenAjuda)
		 * / taxaCLT))) + "*"); txtFaturamentoPJAtual.setText(formatoP
		 * .format((1 - (custoPJTx / taxaCLT))));
		 */

		textoEnviar = "Cálculo de Taxa Ideal    " + "\nTaxa Ideal CLT:"
				+ txtTaxaMinimaCLT.getText() + "\nTaxa Atual:"
				+ txtTaxaAtual.getText() + "\n\to Faturamento:"
				+ txtFaturamentoCLT.getText() + "\n\to Custos:"
				+ txtCustosCLT.getText() + "\n\t\t• Salário:"
				+ txtCustosSalarioCLT.getText() + "\n\t\t• Imposto Fatura:"
				+ txtCustosImpostoFaturaCLT.getText()
				+ "\n\t\t• Encargo+Benef.:" + txtCustosEncargosCLT.getText()
				+ "\n\to Resultado:" + txtResultadoMCLT.getText() + " - "
				+ txtResultadoPCLT.getText() + "\n\to Resultado taxa atual:"
				+ txtResultadoPCLTAtual.getText()
				+ "\n* Ajuda de Custo abate no % Total CLT" + "\n"
				+ "\nTaxa Ideal PJ:" + txtTaxaMinimaPJ.getText()
				+ "\nTaxa Atual:" + txtTaxaAtual.getText()
				+ "\n\to Faturamento:" + txtFaturamentoPJ.getText()
				+ "\n\to Custos:" + txtCustosPJ.getText() + "\n\t\t• Valor PJ:"
				+ txtCustosSalarioPJ.getText() + "\n\t\t• Imposto Fatura:"
				+ txtCustosImpostoFaturaPJ.getText() + "\n\to Resultado:"
				+ txtResultadoMPJ.getText() + " - " + txtResultadoPPJ.getText()
				+ "\n\to Resultado taxa atual:"
				+ txtFaturamentoPJAtual.getText() + "\n" + "\nConsiderações:"
				+ "\n\to Benefícios:" + txtBeneficio.getText()
				+ "\n\to Ajuda de Custo:" + txtAjuda.getText()
				+ "\n\nEncargos:" + "\n\to Encargo CLT:" + txtEncargo.getText()
				+ "\n\to Imposto Fatura:" + txtImpostoFatura.getText()
				+ "\n\to Dissídio:" + txtdissidio.getText();

	}

	public void preencheConfig() {
		txtDiasUteis = (TextView) rootView.findViewById(R.id.tvDiaUtilV);
		txtHora = (TextView) rootView.findViewById(R.id.tvHoraDiaV);
		txtAlimentacao = (TextView) rootView.findViewById(R.id.tvAlimentacaoV);
		txtTransporte = (TextView) rootView.findViewById(R.id.tvTransporteV);
		txtOutros = (TextView) rootView.findViewById(R.id.tvOutrosV);
		txtEncargo = (TextView) rootView.findViewById(R.id.tvEncargoV);
		txtImpostoFatura = (TextView) rootView
				.findViewById(R.id.tvImpostoFaturaV);
		txtdissidio = (TextView) rootView.findViewById(R.id.tvdissidioV);

		txtBeneficio = (TextView) rootView.findViewById(R.id.txtBeneficio);
		txtAjuda = (TextView) rootView.findViewById(R.id.tvAjudaV);
	}

	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences.Editor editor = preferences.edit();
		editor.commit();
		editor.apply();
		loadPreferences();

	}

}