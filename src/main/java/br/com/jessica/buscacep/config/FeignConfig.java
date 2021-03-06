package br.com.jessica.buscacep.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import feign.Logger;
import feign.codec.Decoder;
import feign.okhttp.OkHttpClient;

@Configuration
public class FeignConfig {

	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}

	@Bean
	public OkHttpClient client() {
		return new OkHttpClient();
	}

	@Bean
	public Decoder feignDecoder() {
		return new ResponseEntityDecoder(new SpringDecoder(feignHttpMessageConverter()));
	}

	public ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
		final HttpMessageConverters httpMessageConverters = new HttpMessageConverters(
				new MappingJackson2HttpMessageConverter());
		return new ObjectFactory<HttpMessageConverters>() {
			@Override
			public HttpMessageConverters getObject() throws BeansException {
				return httpMessageConverters;
			}
		};
	}

	public class MappingJackson2HttpMessageConverter
			extends org.springframework.http.converter.json.MappingJackson2HttpMessageConverter {
		MappingJackson2HttpMessageConverter() {
			List<MediaType> mediaTypes = new ArrayList<>();
			mediaTypes.add(MediaType.valueOf(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"));
			setSupportedMediaTypes(mediaTypes);
		}
	}

}