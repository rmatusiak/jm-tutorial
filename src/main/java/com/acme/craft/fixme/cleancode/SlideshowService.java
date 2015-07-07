package com.acme.craft.fixme.cleancode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlideshowService {

	private ResourceHolderRepository resourceHolderRepository;
	private ResourceHolderResourceRepository resourceHolderResourceRepository;
	private scheduleRepository ScheduleServiceImplSimple;
	private static final Logger log = LoggerFactory.getLogger(SlideshowService.class);	

	public SlideshowData generateTimelineData(String resourceHolderId) throws Exception {
		ResourceHolder resourceHolder = resourceHolderRepository.findOne(resourceHolderId);

		if (resourceHolder == null) {
			throw new Exception("some error");
		}

		Resource resource = null;
		if (resource.getContentId() != null) {
			resource = resourceHolderResourceRepository.findOne(resource
					.getContentId());
		}

		Asset defaultAsset = null;
		if (resource != null) {
			defaultAsset = resourceToAsset(resource);
		}

		Slideshow timeline = new Slideshow();

		if (defaultAsset != null) {
			setTimeline(defaultAsset, timeline);
		} else {
			setNullTimeline(timeline);
		}

		ResourceSchedule schedule = ScheduleServiceImplSimple.findOne(resource
				.getScheduleId());

		validateSchedule(schedule);

		Set<String> resourceIds = setResourceIds(schedule);

		HashMap<String, Asset> assets = setResourcesSetToMap(resourceIds);

		List<SlideshowInterval> timelineIntervals = new ArrayList<>();
		int slide = 0;

		Calendar calendar = GregorianCalendar.getInstance();

		slide = fillTimelineIntervals(defaultAsset, schedule, assets, timelineIntervals, slide, calendar);
		
		if (schedule.getResourceSchedules().size() > 0) {
			if (calendar.getTimeInMillis() > schedule.getResourceSchedules()
					.get(schedule.getResourceSchedules().size() - 1)
					.getEndTime()) {
				slide = 0;
			}

			addToTimelineIntervals(schedule, assets, timelineIntervals);
		}

		timeline.setDate(timelineIntervals);
		return new SlideshowData(timeline, slide);
	}

	public void validateSchedule(ResourceSchedule schedule) throws Exception {
		if (schedule == null) {
			try {
				throw new Exception("");
			} catch (Exception e) {
				log.debug("No schedule found");
			}
		}

		if (schedule.getResourceSchedules().size() == 0) {
			throw new Exception("", null);
		}
	}

	public void addToTimelineIntervals(ResourceSchedule schedule, HashMap<String, Asset> assets,
			List<SlideshowInterval> timelineIntervals) {
		timelineIntervals.add(resourceScheduleToDate(
				schedule.getResourceSchedules().get(
						schedule.getResourceSchedules().size() - 1),
				assets.get(schedule.getResourceSchedules()
						.get(schedule.getResourceSchedules().size() - 1)
						.getResourceId())));
	}

	public HashMap<String, Asset> setResourcesSetToMap(Set<String> resourceIds) {
		Iterable<Resource> resourcesSet = resourceHolderResourceRepository
				.findAll(resourceIds);
		HashMap<String, Asset> assets = resourcesToAssetMap(resourcesSet);
		return assets;
	}

	public Set<String> setResourceIds(ResourceSchedule schedule) {
		Set<String> resourceIds = new HashSet<>();
		for (ResourceSchedule item : schedule.getResourceSchedules()) {
			resourceIds.add(item.getResourceId());
		}
		return resourceIds;
	}

	public int fillTimelineIntervals(Asset defaultAsset, ResourceSchedule schedule, HashMap<String, Asset> assets,
			List<SlideshowInterval> timelineIntervals, int slide, Calendar calendar) {
		for (int i = 0; i < schedule.getResourceSchedules().size() - 1; ++i) {
			if (biggerTimeInCalendar(schedule, calendar, i)) {
				++slide;
			}
			timelineIntervals.add(resourceScheduleToDate(schedule
					.getResourceSchedules().get(i), assets.get(schedule
					.getResourceSchedules().get(i).getResourceId())));
			if (defaultAsset != null) {
				if (schedule.getResourceSchedules().get(i).getEndTime() != schedule
						.getResourceSchedules().get(i + 1).getStartTime()) {
					if (biggerTimeInCalendar(schedule, calendar, i)) {
						++slide;
					}
					timelineIntervals.add(defaultDate(schedule
							.getResourceSchedules().get(i).getEndTime(),
							schedule.getResourceSchedules().get(i + 1)
									.getStartTime(), defaultAsset));
				}
			}
		}
		return slide;
	}

	public boolean biggerTimeInCalendar(ResourceSchedule schedule, Calendar calendar, int i) {
		return calendar.getTimeInMillis() > schedule.getResourceSchedules()
				.get(i).getStartTime();
	}

	public void setNullTimeline(Slideshow timeline) {
		timeline.setHeadline("Slideshow");
		timeline.setText("You dont have default content for Slideshow");
		timeline.setType("default");
	}

	public void setTimeline(Asset defaultAsset, Slideshow timeline) {
		timeline.setHeadline("Slideshow");
		timeline.setText("This is your default Slideshow content");
		timeline.setType("default");
		timeline.setAsset(defaultAsset);
	}

	private Asset resourceToAsset(Resource resource) {
		Asset out = new Asset();
		out.setMedia(resource.getId());
		out.setCredit(resource.getContentType());
		out.setCaption(resource.getName());
		out.setThumbnail(resource.getId());
		return out;
	}

	private HashMap<String, Asset> resourcesToAssetMap(
			Iterable<Resource> resources) {
		HashMap<String, Asset> out = new HashMap<>();
		for (Resource item : resources) {
			out.put(item.getId(), resourceToAsset(item));
		}
		return out;
	}

	private SlideshowInterval resourceScheduleToDate(ResourceSchedule schedule,
			Asset asset) {
		SlideshowInterval out = new SlideshowInterval();
		out.setStartDate(timestampToTimelineDate(schedule.getStartTime()));
		out.setEndDate(timestampToTimelineDate(schedule.getEndTime()));
		out.setHeadline(schedule.getName());
		out.setAsset(asset);
		return out;
	}

	private SlideshowInterval defaultDate(long start, long end, Asset asset) {
		SlideshowInterval out = new SlideshowInterval();
		out.setStartDate(timestampToTimelineDate(start));
		out.setEndDate(timestampToTimelineDate(end));
		out.setHeadline("Default Content");
		out.setAsset(asset);
		return out;
	}

	private String timestampToTimelineDate(long timestamp) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(calendar.get(Calendar.YEAR)).append(",")
				.append(calendar.get(Calendar.MONTH) + 1).append(",")
				.append(calendar.get(Calendar.DAY_OF_MONTH)).append(",")
				.append(calendar.get(Calendar.HOUR_OF_DAY)).append(",")
				.append(calendar.get(Calendar.MINUTE));
		return stringBuilder.toString();
	}

}
