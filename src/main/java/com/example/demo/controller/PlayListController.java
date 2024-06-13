package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.demo.entities.PlayList;
import com.example.demo.entities.Songs;
import com.example.demo.services.PlayListService;
import com.example.demo.services.SongsService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class PlayListController {
	
	@Autowired
	PlayListService pserv;
	
	@Autowired
	SongsService sserv;
	
	@GetMapping("/createplaylist")
	public String createPlayList(Model model) {
		
		//fetching the songs using song service 
		List<Songs> songslist = sserv.fetchAllsongs();
		//adding the song in the model
		model.addAttribute("songslist",songslist);
		//sending create playlist
		return "createplaylist";
	}
	
	
	@PostMapping("/addplaylist")
	public String addPlaylist(@ModelAttribute PlayList playlist) {
		//adding plylist
		pserv.addPlaylist(playlist);
		
		//update song playlist
		List<Songs>songsList = playlist.getSong();
		for(Songs song : songsList) {
			song.getPlaylist().add(playlist);
			sserv.updateSong(song);
		}
		return "playlistsuccess";
	}
	
	
}
