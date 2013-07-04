package com.example.odata;

import java.util.List;
import java.util.Map;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String TAG = "OData";
	ListView listView1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView1 = (ListView) MainActivity.this.findViewById(R.id.listView1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void doQuery(View view) {
		TextView textView = (TextView) this.findViewById(R.id.dataSource);
		RetreiveDataTask retreiveDataTask = new RetreiveDataTask();
		retreiveDataTask.execute(textView.getText().toString());

	}

	class RetreiveDataTask extends AsyncTask<String, Void, Void> {

		List<Map> fields;
		List<Map<String, Object>> records;

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(null);
			String[] from = new String[] { "no", "police_office" };
			int[] to = new int[] { R.id.no, R.id.police_office };
			Log.v(TAG, "records:" + records);
			SimpleAdapter adapter = new SimpleAdapter(MainActivity.this,
					records, R.layout.grid_row, from, to);
			listView1.setAdapter(adapter);
		}

		protected Void doInBackground(String... urls) {
			try {

				RestTemplate restTemplate = new RestTemplate();
				restTemplate.getMessageConverters().add(
						new MappingJacksonHttpMessageConverter());
				Map map = restTemplate.getForObject(urls[0], Map.class);
				Map result = (Map) map.get("result");
				List<Map> fields = (List) result.get("fields");
				records = (List) result.get("records");

				// if (!CollectionUtils.isEmpty(records)) {
				// for (Map record : records) {
				// if (!CollectionUtils.isEmpty(fields)) {
				// for (Map field : fields) {
				// Log.v(TAG, "id:" + field.get("id").toString());
				// listView1.
				// }
				// }
				// }
				// }

				Log.v(TAG, map.toString());

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return null;

		}

	}

}
