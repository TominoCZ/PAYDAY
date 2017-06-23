package com.TominoCZ.PAYDAY.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import com.TominoCZ.PAYDAY.PAYDAY;

import scala.reflect.io.Directory;

public class MConfigHandler {
	static FileInputStream fis;
	static InputStreamReader isr;
	static BufferedReader br;

	static File f;

	public static void init() {
		try {
			f = PAYDAY.config;

			if (!f.exists()) {
				if (!Directory.apply(f.getParent()).exists())
					Directory.apply(f.getParent()).createDirectory(true, false);

				f.createNewFile();

				write();
			}

			read();

			write();

			closeStreams();
		} catch (IOException e) {
			closeStreams();

			write();
		}
	}

	public static void write() {
		try {
			check();

			PrintWriter writer = new PrintWriter(f.getPath(), "UTF-8");
			// writer.println("fuelID=" + PAYDAY.fuelID); TODO
			writer.close();
		} catch (Exception e) {
			closeStreams();

			if (!f.exists()) {
				if (!Directory.apply(f.getParent()).exists())
					Directory.apply(f.getParent()).createDirectory(true, false);

				try {
					f.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			write();
		}
	}

	static void read() {
		try {
			fis = new FileInputStream(f);
			isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			br = new BufferedReader(isr);

			String line;

			while ((line = br.readLine()) != null) {
				// if (line.contains("fuelID=")) TODO
				// PAYDAY.fuelID = Integer.valueOf(line.replaceAll(" ",
				// "").replace("fuelID=", ""));
			}

			closeStreams();

			check();
		} catch (Exception e) {
			closeStreams();

			check();

			write();
		}
	}

	static void closeStreams() {
		try {
			br.close();
			isr.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void check() {
		// TODO
	}
}
