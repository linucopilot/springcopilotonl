package com.lincopilot.springcopilotonl.common.util;

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

/**
 * JSON 처리를 위한 유틸리티 모음
 *
 * @author ymkim 
 * @version 1.0
 * @since 2020. 7. 28.
 */
public final class JsonUtils {

	private JsonUtils() {}

	private static ObjectMapper mapper = createNewObjectMapper(true);
	private static ObjectMapper mapperNonEmpty = createNewObjectMapper(false);
	private static final String DEFAULT_MSG = "Unable to read JSON node from input '%s'";

	public static ObjectMapper getObjectMapper() {
		return mapper;
	}

	public static ObjectMapper getObjectMapperNonEmpty() {
		return mapperNonEmpty;
	}

	public static ObjectMapper createNewObjectMapper(boolean includeType) {
		final ObjectMapper mapper = JsonMapper.builder()
				.enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)
				.disable(MapperFeature.DEFAULT_VIEW_INCLUSION)
				.build();
		if (includeType) {
			mapper.setDefaultPropertyInclusion(JsonInclude.Include.ALWAYS);
		} else {
			mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY);
		}
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		return mapper;
	}

	public static <T> T convertValue(Object object, Class<T> clazz) {
		try {
			if (object == null) {
				return null;
			} else {
				return mapper.convertValue(object, clazz);
			}
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(
					format("Unable to read object '%s' from input '%s'", clazz.getSimpleName(), object), e);
		}
	}

	public static <T> T readJson(final String jsonString, final Class<T> clazz) {
		try {
			return mapper.readValue(jsonString, clazz);
		} catch (IOException e) {
			throw new IllegalArgumentException(
					format("Unable to read JSON object '%s' from input '%s'", clazz.getSimpleName(), jsonString), e);
		}
	}

	public static <T> T readJson(final String jsonString, final TypeReference<T> typeReference) {
		try {
			return mapper.readValue(jsonString, typeReference);
		} catch (IOException e) {
			throw new IllegalArgumentException(
					format("Unable to read JSON object '%s' from input '%s'", typeReference.getType(), jsonString), e);
		}
	}

	public static JsonNode readJson(final String jsonString) {
		try {
			return mapper.readTree(jsonString);
		} catch (IOException e) {
			throw new IllegalArgumentException(format(DEFAULT_MSG, jsonString), e);
		}
	}

	public static Map<String, Object> readJsonToMap(final String jsonString) {
		return readJson(jsonString, new TypeReference<HashMap<String, Object>>() {});
	}

	public static List<Map<String, Object>> readJsonToList(final String jsonString) {
		return readJson(jsonString, new TypeReference<List<Map<String, Object>>>() {});
	}

	public static <T> String toJson(final T object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (final JsonProcessingException e) {
			throw new IllegalArgumentException(format("Unable to write JSON object '%s'", object.getClass().getSimpleName()), e);
		}
	}

	public static <T> JsonNode toJsonNode(final T object) {
		final String jsonString = toJson(object);
		return readJson(jsonString);
	}

	public static <T> String toJson(final T object, ObjectMapper mapper) {
		try {
			return mapper.writeValueAsString(object);
		} catch (final JsonProcessingException e) {
			throw new IllegalArgumentException(format("Unable to write JSON object '%s'", object.getClass().getSimpleName()), e);
		}
	}

	public static <T> T readJsonFromFile(final String resourcePath, final Class<T> clazz) {
		try {
			File file = new File(resourcePath);
			String result = java.nio.file.Files.readString(file.toPath(), StandardCharsets.UTF_8);
			return mapper.readValue(result, clazz);
		} catch (IOException e) {
			throw new IllegalArgumentException(format("Unable to read JSON object '%s' from resource path '%s'",
					clazz.getSimpleName(), resourcePath), e);
		}
	}

	public static JsonNode readJsonNodeFromFile(final String resourcePath) {
		File file = new File(resourcePath);
		try (var bufferedReader = java.nio.file.Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
			return mapper.readTree(bufferedReader);
		} catch (IOException e) {
			throw new IllegalArgumentException(format("Unable to read JSON node from resource path '%s'", resourcePath), e);
		}
	}

	public static void writeJsonToFile(final String resourcePath, final String jsonString) {
		try {
			File file = new File(resourcePath);
			java.nio.file.Files.writeString(file.toPath(), jsonString, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new IllegalArgumentException(format("Unable to write file to resourcePath '%s'", resourcePath), e);
		}
	}

	public static String unprettify(final String jsonString) {
		try {
			JsonNode node = mapper.readValue(jsonString, JsonNode.class);
			return mapper.writeValueAsString(node);
		} catch (IOException e) {
			throw new IllegalArgumentException(format(DEFAULT_MSG, jsonString), e);
		}
	}

	public static String prettify(final String jsonString) {
		try {
			JsonNode node = mapper.readValue(jsonString, JsonNode.class);
			ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
			return writer.writeValueAsString(node);
		} catch (IOException e) {
			throw new IllegalArgumentException(format(DEFAULT_MSG, jsonString), e);
		}
	}

	public static String prettify(final Object object) {
		try {
			JsonNode node = mapper.convertValue(object, JsonNode.class);
			ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
			return writer.writeValueAsString(node);
		} catch (IOException e) {
			throw new IllegalArgumentException(format(DEFAULT_MSG, object), e);
		}
	}
	
	
	
	
	public static String mapToJson(Map<String, Object> data) throws Exception {
	    
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	    objectMapper.enable(SerializationFeature.INDENT_OUTPUT); 
	    return objectMapper.writeValueAsString(data);
	}
	
	private static final ObjectMapper objectMapper;
	static {
	    objectMapper = new ObjectMapper();
	    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
	    objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
	}
	public static Map<String, Object> readJsonToMap2(String json) throws Exception {
	    if (json == null || !json.trim().startsWith("{")) {
	        throw new IllegalArgumentException("Input is not a valid JSON Object. Content: " + json);
	    }
	    
	    try {
	        return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
	    } catch (JsonProcessingException e) {
	        throw new Exception("Jackson failed to parse JSON: " + e.getMessage());
	    }
	}
}
