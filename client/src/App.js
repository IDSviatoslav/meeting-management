import React, { useState } from "react";
import MeetingEditor from "./Components/MeetingEditor";
import MeetingsView from "./Components/MeetingsView";

function App() {
  const [workMode, setWorkMode] = useState("VIEW");
  const [meetingId, setMeetingId] = useState();

  function displayMeeting(id) {
    if (id == null) {
      setWorkMode("CREATE");
    } else {
      setWorkMode("EDIT");
      setMeetingId(id);
    }
    console.log("in app id: " + id);
  }

  function viewMeetings() {
    setWorkMode("VIEW");
  }

  var form =
    workMode === "VIEW" ? (
      <MeetingsView DisplayMeeting={displayMeeting} />
    ) : (
      <MeetingEditor
        ViewMeetings={viewMeetings}
        meetingId={meetingId}
        workMode={workMode}
      />
    );
  return <div className="wrapper">{form}</div>;
}

export default App;
