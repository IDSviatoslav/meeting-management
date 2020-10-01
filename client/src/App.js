import React, { useState } from "react";
import MeetingEditor from "./Components/MeetingEditor";
import MeetingsView from "./Components/MeetingsView";

function App() {
  const [workMode, setWorkMode] = useState("VIEW");
  function displayMeeting(id) {
    setWorkMode("EDIT");
  }
  var form =
    workMode === "EDIT" ? (
      <MeetingEditor />
    ) : (
      <MeetingsView DisplayMeeting={displayMeeting} />
    );
  return <div className="wrapper">{form}</div>;
}

export default App;
