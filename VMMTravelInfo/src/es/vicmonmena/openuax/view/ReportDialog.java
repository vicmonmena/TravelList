package es.vicmonmena.openuax.view;

import es.vicmonmena.openuax.R;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Dialog personalizado que actúa como Listener propio.
 * </br>
 * El Dialog permite reportar errores, mejoras u observaciones dela aplicación 
 * enviando un email.
 * 
 * @author vicmonmena
 *
 */
public class ReportDialog extends Dialog implements OnClickListener {

	private Context context;
	
	public ReportDialog(Context context) {
		super(context);
		this.context = context;
		setTitle(R.string.report_title);
    	setContentView(R.layout.report_dialog);
    	((Button) findViewById(R.id.report_send)).setOnClickListener(this);
    	((Button) findViewById(R.id.report_cancel)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.report_send:
				
				// Comprobamos que estén rellenos todos los campo
				EditText message = (EditText) findViewById(R.id.report_message);
				Spinner subject = (Spinner)findViewById(R.id.report_task_type);
				
				if (!TextUtils.isEmpty(message.getText())) {
					sendEmail(context.getString(R.string.support_email_address), 
						subject.getSelectedItem().toString(), 
						message.getText().toString());
					dismiss();
				} else {
					Toast.makeText(context, 
						context.getString(R.string.report_validation), 
						Toast.LENGTH_SHORT).show();
				}
		        
				break;
	
			default:
				dismiss();
				break;
		}
		
	}
	
	/**
	 * Envía un email con los parámetros del método.
	 * @param address
	 * @param subject
	 * @param message
	 */
	private void sendEmail(String address, String subject, String message) {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{address});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, 
        	context.getString(R.string.app_name) + " - " + subject);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        emailIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(emailIntent, "Sending email..."));

	}

}
