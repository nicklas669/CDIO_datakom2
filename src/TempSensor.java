import java.util.Random;


public class TempSensor implements Runnable {
	private double temperature;
	private long sampleRate;
	private Random rnd = new Random();

	public TempSensor() {
		this.temperature = 21.3;
		this.sampleRate = 10000;
	}

	public String getTemperature() {
		return String.format("%.2f", temperature);
	}

	public long getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(long sampleRate) {
		System.out.println("SAMPLE RATE SÆTTES TIL " + sampleRate);
		this.sampleRate = sampleRate;
	}

	@Override
	public void run() {
		while (true) {
//			System.out.println("SAMPLERATE ER :"+sampleRate);
			if (sampleRate > 0) {
				temperature = (rnd.nextInt(55)+2)+(1*rnd.nextFloat());
				try {
					this.wait(sampleRate);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				try {
					Thread.sleep(sampleRate);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
