package io.github.nuclearfarts.cbt.resource;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import com.google.common.collect.ImmutableSet;

import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.metadata.ResourceMetadataReader;
import net.minecraft.util.Identifier;

import io.github.nuclearfarts.cbt.tile.Tile;

public class CBTResourcePack implements ResourcePack {
	
	private static final Set<String> NAMESPACES = ImmutableSet.of("connectedblocktextures");
	
	private final Map<String, Identifier> aliases = new HashMap<>(); 
	private final Map<String, byte[]> resources = new HashMap<>();
	private final ResourceManager manager;
	
	private int genCounter = 0;
	
	public CBTResourcePack(ResourceManager manager) {
		this.manager = manager;
	}
	
	public void putResource(String resource, byte[] data) {
		resources.put(resource, data);
	}
	
	public void alias(String location, Identifier to) {
		aliases.put(location, to);
	}
	
	public void putImage(String location, BufferedImage image) {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "png", byteOut);
		} catch (IOException e) {
			throw new RuntimeException("IOException writing to bytearrayoutputstream!?", e);
		}
		putResource(location, byteOut.toByteArray());
	}
	
	public Identifier dynamicallyPutImage(BufferedImage image) {
		String texPath = "gen/" + genCounter++;
		putImage("assets/connectedblocktextures/textures/" + texPath + ".png", image);
		return new Identifier("connectedblocktextures", texPath);
	}
	
	public Identifier dynamicallyPutTile(Tile tile) {
		String texPath = "gen/" + genCounter++;
		putTile("assets/connectedblocktextures/textures/" + texPath + ".png", tile);
		return new Identifier("connectedblocktextures", texPath);
	}
	
	public void putTile(String location, Tile tile) {
		if(tile.hasResource()) {
			alias(location, tile.getResource());
		} else {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			try {
				ImageIO.write(tile.getImage(), "png", byteStream);
			} catch (IOException e) {
				throw new RuntimeException("IOException writing to byte array output stream!?", e);
			}
			resources.put(location, byteStream.toByteArray());
		}
	}

	@Override
	public void close() {}

	@Override
	public InputStream openRoot(String fileName) throws IOException {
		byte[] data;
		Identifier aliasOf;
		if((data = resources.get(fileName)) != null) {
			return new ByteArrayInputStream(data);
		} else if((aliasOf = aliases.get(fileName)) != null) {
			return manager.getResource(aliasOf).getInputStream();
		}
		throw new IOException("generated resources pack has no data or alias for " + fileName);
	}

	@Override
	public InputStream open(ResourceType type, Identifier id) throws IOException {
		if(type == ResourceType.SERVER_DATA) throw new IOException("reading server data from connectedblocktextures client resource pack");
		return openRoot(type.getDirectory() + "/" + id.getNamespace() + "/" + id.getPath());
	}

	@Override
	public Collection<Identifier> findResources(ResourceType type, String namespace, String prefix, int maxDepth, Predicate<String> pathFilter) {
		//maxdepth not implemented.
		if(type == ResourceType.SERVER_DATA) return Collections.emptyList();
		String start = "assets/" + namespace + "/" + prefix;
		return resources.keySet().stream().filter(s -> s.startsWith(start) && pathFilter.test(s)).map(CBTResourcePack::fromPath).collect(Collectors.toList());
	}

	@Override
	public boolean contains(ResourceType type, Identifier id) {
		String path = type.getDirectory() + "/" + id.getNamespace() + "/" + id.getPath();
		return resources.containsKey(path) || aliases.containsKey(path);
	}

	@Override
	public Set<String> getNamespaces(ResourceType type) {
		return NAMESPACES;
	}

	@Override
	public <T> T parseMetadata(ResourceMetadataReader<T> metaReader) throws IOException {
		return null;
	}
	
	@Override
	public String getName() {
		return "Connected Block Textures generated resources";
	}

	private static Identifier fromPath(String path) {
		String[] split = path.split("/", 2);
		return new Identifier(split[0], split[1]);
	}
}
